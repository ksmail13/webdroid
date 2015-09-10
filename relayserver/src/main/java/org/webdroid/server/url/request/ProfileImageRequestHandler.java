package org.webdroid.server.url.request;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.Query;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JsonUtil;

import java.util.Map;

/**
 * Created by micky on 2015. 9. 7..
 */
public class ProfileImageRequestHandler extends RequestHandler {

    Vertx vertx = null;

    public ProfileImageRequestHandler(DBConnector dbConnector, Vertx vertx) {
        super(dbConnector, false, "id");

        this.vertx = vertx;
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        JsonArray reqParams = JsonUtil.createJsonArray(params.get("id"));

        mDBConnector.query(Query.GET_PROFILE_IMAGE, reqParams, new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {
                String path = resultSet.getRows().get(0).getString("path");
                if(path != null) {
                    Buffer image = vertx.fileSystem().readFileBlocking(WebdroidConstant.Path.UPLOAD_IMG+path);
                    String[] spPath = path.split(".");
                    String ext = spPath[spPath.length-1];
                    logger.debug("user profile image " + path +" ext : " + ext);
                    send(HttpStatusCode.SUCCESS, "image/"+ext, image);
                }
            }
        });
    }
}
