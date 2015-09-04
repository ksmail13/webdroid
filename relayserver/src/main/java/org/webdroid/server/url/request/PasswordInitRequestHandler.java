package org.webdroid.server.url.request;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.UpdateResult;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JsonUtil;

import java.util.Map;

/**
 * Created by micky on 2015. 9. 5..
 */
public class PasswordInitRequestHandler extends RequestHandler {

    public static final String URL = "/api/pwfind";

    public PasswordInitRequestHandler(DBConnector dbConnector) {
        super(dbConnector, true, "user_id");
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(params.get("user_id"));

        mDBConnector.update(Query.SET_RANDOM_PW, dbParams, new SQLResultHandler<UpdateResult>(this) {
            @Override
            public void success(UpdateResult result) {
                if (result.getUpdated() > 0) {
                    sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.SET_RANDOM_PW);
                    //redirectTo("/");
                } else
                    sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.SET_RANDOM_PW_FAIL);
            }
        });
    }
}
