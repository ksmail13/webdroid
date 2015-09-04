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
public class UnsubscribeRequestHandler extends RequestHandler {

    public static final String URL = "/unsubscribe";

    public UnsubscribeRequestHandler(DBConnector dbConnector) {
        super(dbConnector, true);
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray((Integer) session.get("id"));

        if(session.get("isPasswordChecked")) {

            mDBConnector.update(Query.UNSUBSCRIBE, dbParams,
                    new SQLResultHandler<UpdateResult>(this) {
                        @Override
                        public void success(UpdateResult result) {
                            if (result.getUpdated() > 0) {
                                sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.MEMBER_CHECKED);
                            } else
                                sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.MEMBER_FAIL);
                        }
                    });
        } else {
            sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.IRRAGULAR_ACCESS);
        }
    }
}
