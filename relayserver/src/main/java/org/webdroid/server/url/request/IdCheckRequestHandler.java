package org.webdroid.server.url.request;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
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
public class IdCheckRequestHandler extends RequestHandler {
    public static final String URL = "/idcheck";

    public IdCheckRequestHandler(DBConnector dbConnector) {
        super(dbConnector, false, "user_id");
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(params.get("user_id"));

        mDBConnector.query(Query.ID_CHECK, dbParams, new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {

                if (resultSet.getRows().get(0).getInteger("cnt") == 0) {
                    sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.ID_CHECK);
                } else {
                    sendJsonResult(HttpStatusCode.SUCCESS, false    , ResultMessage.ID_CHECK_ERROR);
                }
            }
        });
    }
}
