package org.webdroid.server.url.request;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.ResultMessage;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
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

    public ProjectUploadRequestHandler(DBConnector dbConnector, Vertx vertx) {
        super(dbConnector, true, "p_name", "p_descript");
        mDBConnector = dbConnector;
        this.vertx = vertx;
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        Set<FileUpload> uploads = context.fileUploads();
        uploads.stream().forEach(upload -> this.upload = upload);

        String pProjectPath = WebdroidConstant.Path.UPLOAD_PROJECT+"/"+String.format("%d_%s",1,req.getParam("p_name"));
        String pProjectName = WebdroidConstant.Path.UPLOAD_PROJECT+"/"+String.format("%d_%s_%s",1,req.getParam("p_name"),upload.fileName());
        logger.debug(String.format("project file save in %s", pProjectName));
        logger.debug(upload);

        //UnZipper.unZipper("");
        //vertx.fileSystem().mkdir(pProjectPath, voidAsyncResult -> {});
        vertx.fileSystem().copy(upload.uploadedFileName(), pProjectName, voidAsyncResult -> {
        });
        logger.debug("uploads");
        uploads.forEach(upload -> logger.debug(upload.fileName() + " " + upload.uploadedFileName() + " " + upload.name()));

        //sendJsonResult(HttpStatusCode.SUCCESS, false, "not implement");
        JsonObject res = JsonUtil.createJsonResult(true, ResultMessage.SUCCESS);
        send(HttpStatusCode.SUCCESS, "application/json", res.toString());
    }
}
