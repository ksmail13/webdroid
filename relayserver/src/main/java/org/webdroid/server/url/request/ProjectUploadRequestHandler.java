package org.webdroid.server.url.request;

import io.vertx.ext.web.FileUpload;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.db.DBConnector;
import org.webdroid.server.handler.RequestHandler;

import java.util.Map;
import java.util.Set;

/**
 * Created by micky on 2015. 9. 10..
 */
public class ProjectUploadRequestHandler extends RequestHandler {

    public static final String URL = "/fileup";

    public ProjectUploadRequestHandler(DBConnector dbConnector) {
        super(dbConnector, true, "p_name", "p_descript");
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        Set<FileUpload> uploads = context.fileUploads();

    logger.debug("uploads");
    uploads.forEach(upload -> logger.debug(upload.fileName() + " " + upload.uploadedFileName() + " " + upload.name()));

    sendJsonResult(HttpStatusCode.SUCCESS, false, "not implement");
}
}
