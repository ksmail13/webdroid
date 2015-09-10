package org.webdroid.server.url.request;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.FileUpload;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RouteHandler;
import org.webdroid.util.JsonUtil;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by micky on 2015. 9. 5..
 */
public class UserImageUploadReqeustHandler extends RouteHandler {

    public static final String URL = "/p_img_upload";
    private DBConnector mDBConnector = null;

    private Vertx vertx = null;

    FileUpload upload;

    public UserImageUploadReqeustHandler(DBConnector dbConnector, Vertx vertx) {
        mDBConnector = dbConnector;
        this.vertx = vertx;
    }

    @Override
    public void handling() {
        if(!isLogin()) {
            sendJsonResult(HttpStatusCode.UNAUTHORIZED_ACCESS, false, ResultMessage.UNAUTHORIZED_ACCESS);
            return;
        }

        Set<FileUpload> uploads = context.fileUploads();
        uploads.stream().forEach(upload -> this.upload = upload);
        Calendar c = Calendar.getInstance();


        String pImageName = WebdroidConstant.Path.UPLOAD_IMG+"/"+String.format("%d_profile_%d.%s",
                (Integer)session.get("id"), c.getTimeInMillis(), upload.fileName());
        logger.debug(String.format("file save in %s", pImageName));
        logger.debug(upload);

        vertx.fileSystem().copyBlocking(upload.uploadedFileName(), pImageName);

        JsonArray dbParms = JsonUtil.createJsonArray(pImageName, session.get("id"));
        mDBConnector.update(Query.PROFILE_IMG_UPLOAD, dbParms, new SQLResultHandler<UpdateResult>(this) {
            @Override
            public void success(UpdateResult resultSet) {
                JsonObject res = JsonUtil.createJsonResult(true, ResultMessage.SUCCESS).put("uploadPath", pImageName);
                send(HttpStatusCode.SUCCESS, "application/json", res.toString());
            }
        });
    }
}
