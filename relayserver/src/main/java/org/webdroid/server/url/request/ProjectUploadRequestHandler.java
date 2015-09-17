package org.webdroid.server.url.request;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.FileUpload;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JsonUtil;
import org.webdroid.util.UnZipper;

import java.util.Map;
import java.util.Set;

/**
 * Created by micky on 2015. 9. 10..
 */
public class ProjectUploadRequestHandler extends RequestHandler {

    public static final String URL = "/fileup";
    private Vertx vertx = null;
    FileUpload upload;
    Set<FileUpload> uploads;
    private int pid;

    public ProjectUploadRequestHandler(DBConnector dbConnector, Vertx vertx) {
        super(dbConnector, true, "p_name", "p_descript");
        mDBConnector = dbConnector;
        this.vertx = vertx;
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        uploads = context.fileUploads();
        if(uploads.size() > 0) {
            JsonArray dbParams = JsonUtil.createJsonArray(params.get("p_name"), params.get("p_descript"), session.get("id"));
            mDBConnector.update(Query.NEW_PROJECT, dbParams, new SQLResultHandler<UpdateResult>(this) {
                @Override
                public void success(UpdateResult result) {
                    getPid();
                }
            });
        } else {
            sendJsonResult(HttpStatusCode.SUCCESS, false, "no file upload");
        }
    }

    public void getPid(){
        mDBConnector.query("select LAST_INSERT_ID();", null,new SQLResultHandler<ResultSet>(this){
            @Override
            public void success(ResultSet resultSet) {
                pid = resultSet.getResults().get(0).getInteger(0);
                //System.out.println(pid);
                makeProjectPath();
            }
        });
    }

    public void makeProjectPath(){
        uploads.stream().forEach(upload -> this.upload = upload);

        System.out.println(upload.fileName());
        String zipFileName = String.format("%d_%s_%s", pid, req.getParam("p_name"), upload.fileName());
        String unzipPathFolderName = String.format("%d_%s", pid, req.getParam("p_name"));

        String zipFilePath = WebdroidConstant.Path.UPLOAD_PROJECT + "/" + zipFileName;
        String unzipPath = WebdroidConstant.Path.UPLOAD_PROJECT + "/" + unzipPathFolderName + "/";
        logger.debug(String.format("project file save in %s", zipFilePath));
        logger.debug(upload);

        vertx.fileSystem().copy(upload.uploadedFileName(), zipFilePath, voidAsyncResult -> {
            UnZipper.unZipper(zipFileName, unzipPathFolderName);
        });
        logger.debug("uploads");
        uploads.forEach(upload -> logger.debug(upload.fileName() + " " + upload.uploadedFileName() + " " + upload.name()));

        JsonArray dbParams = JsonUtil.createJsonArray(session.get("id"),pid);
        mDBConnector.update(Query.UPDATE_USER_PROJECT, dbParams, new SQLResultHandler<UpdateResult>(this) {
            @Override
            public void success(UpdateResult result) {
            }
        });
        dbParams = JsonUtil.createJsonArray(unzipPath, pid);
        mDBConnector.update(Query.SET_PATH, dbParams, new SQLResultHandler<UpdateResult>(this) {
            @Override
            public void success(UpdateResult result) {
            }
        });

        //sendJsonResult(HttpStatusCode.SUCCESS, false, "not implement");
        JsonObject res = JsonUtil.createJsonResult(true, ResultMessage.SUCCESS);
        send(HttpStatusCode.SUCCESS, "application/json", res.toString());
    }
}
