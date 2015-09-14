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
 * Created by ±è¹Î¼ö on 2015-09-14.
 */
public class CancelProjectfavorateRequestHandler extends RequestHandler{

    public static final String URL = "/cancel_favorate_projectlist";

    public CancelProjectfavorateRequestHandler(DBConnector dbConnector)  {
        super(dbConnector, true,"id");
    }

    @Override

    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(session.get("id").toString());
        mDBConnector.update(Query.CANCEL_FAVORATE_PROJECT, dbParams,
                new SQLResultHandler<UpdateResult>(this) {
                    @Override
                    public void success(UpdateResult resultSet) {
                        if(resultSet.getUpdated()==1)
                            sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.SUCCESS);
                        else
                            sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.SUCCESS);
                    }
                });
    }
}
