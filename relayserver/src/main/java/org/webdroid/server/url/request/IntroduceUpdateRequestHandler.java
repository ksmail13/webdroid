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
public class IntroduceUpdateRequestHandler extends RequestHandler {

    public static final String URL = "/update_introduce";

    public IntroduceUpdateRequestHandler(DBConnector dbConnector) {
        super(dbConnector, true, "introduce");
    }

    @Override

    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(params.get("introduce"), session.get("id"));
        mDBConnector.update(Query.NEW_INTRODUCE, dbParams,
                new SQLResultHandler<UpdateResult>(this) {
                    @Override
                    public void success(UpdateResult resultSet) {
                        sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.SUCCESS);
                    }
                });
    }
}
