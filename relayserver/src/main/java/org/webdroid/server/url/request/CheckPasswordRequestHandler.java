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
public class CheckPasswordRequestHandler extends RequestHandler {

    public static final String URL = "/pwvalidate";

    public CheckPasswordRequestHandler(DBConnector dbConnector) {
        super(dbConnector, true, "old_pw");
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(session.get("id"), params.get("old_pw"));

        mDBConnector.query(Query.PW_CHECK, dbParams, new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {

                if (resultSet.getRows().get(0).getInteger("cnt") == 1) {
                    session.put("isPasswordChecked", true);
                    sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.PW_CHECKED);



                } else {
                    sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.PW_FAIL);
                }
            }
        });
    }
}
