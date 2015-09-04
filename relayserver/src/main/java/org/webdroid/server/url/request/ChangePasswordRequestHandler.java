package org.webdroid.server.url.request;

import io.vertx.core.http.HttpMethod;
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
public class ChangePasswordRequestHandler extends RequestHandler {
    public static final String URL = "/new_pwsubmit";

    public ChangePasswordRequestHandler(DBConnector dbConnector) {
        super(dbConnector, true, "new_pw");
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(params.get("new_pw"), session.get("id"));

        if(session.get("isPasswordChecked")) {

            mDBConnector.update(Query.NEW_PW, dbParams,
                    new SQLResultHandler<UpdateResult>(this) {
                        @Override
                        public void success(UpdateResult result) {
                            if (result.getUpdated() > 0) {
                                sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.PW_CHECKED);
                            } else
                                sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.PW_FAIL);
                        }
                    });
        } else {
            sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.IRRAGULAR_ACCESS);
        }
    }
}
