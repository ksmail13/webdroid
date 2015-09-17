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
        if(uploads.size() > 0) {
            uploads.stream().forEach(upload -> this.upload = upload);

            System.out.println(upload.fileName());
            String zipFileName = String.format("%d_%s_%s", 1, req.getParam("p_name"), upload.fileName());
            String unzipPathFolderName = String.format("%d_%s", 1, req.getParam("p_name"));

            String zipFilePath = WebdroidConstant.Path.UPLOAD_PROJECT + "/" + zipFileName;
            logger.debug(String.format("project file save in %s", zipFilePath));
            logger.debug(upload);

            vertx.fileSystem().copy(upload.uploadedFileName(), zipFilePath, voidAsyncResult -> {
                UnZipper.unZipper(zipFileName, unzipPathFolderName);
            });
            logger.debug("uploads");
            uploads.forEach(upload -> logger.debug(upload.fileName() + " " + upload.uploadedFileName() + " " + upload.name()));

            //sendJsonResult(HttpStatusCode.SUCCESS, false, "not implement");
            JsonObject res = JsonUtil.createJsonResult(true, ResultMessage.SUCCESS);
            send(HttpStatusCode.SUCCESS, "application/json", res.toString());

        } else {
            sendJsonResult(HttpStatusCode.SUCCESS, false, "no file upload");
        }
    }
}
