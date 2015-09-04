package org.webdroid.server.url.request;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.FileUpload;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RouteHandler;
import org.webdroid.util.JsonUtil;

import java.util.Set;

/**
 * Created by micky on 2015. 9. 5..
 */
public class UserImageUploadReqeustHandler extends RouteHandler {

    public static final String URL = "/p_img_upload";
    DBConnector mDBConnector = null;


    String path;

    public UserImageUploadReqeustHandler(DBConnector dbConnector) {
        mDBConnector = dbConnector;
    }

    @Override
    public void handling() {
        Set<FileUpload> uploads = context.fileUploads();
        uploads.stream().forEach(upload -> path = upload.uploadedFileName());
        JsonArray dbParms = JsonUtil.createJsonArray(path, session.get("id"));
        mDBConnector.update(Query.IMG_UPLOAD, dbParms, new SQLResultHandler<UpdateResult>(this) {
            @Override
            public void success(UpdateResult resultSet) {
                JsonObject res = JsonUtil.createJsonResult(true, ResultMessage.SUCCESS).put("img_path", path);
                send(HttpStatusCode.SUCCESS, "application/json", res.toString());
            }
        });
    }
}
