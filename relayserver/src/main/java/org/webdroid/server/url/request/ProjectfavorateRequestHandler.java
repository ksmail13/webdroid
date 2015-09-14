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
 * Created by ��μ� on 2015-09-11.
 */
public class ProjectfavorateRequestHandler extends RequestHandler{

    public static final String URL = "/favorate_projectlist";

    public ProjectfavorateRequestHandler(DBConnector dbConnector)  {
        super(dbConnector, true,"id");
    }

    @Override

    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(session.get("id").toString());
        mDBConnector.update(Query.FAVORATE_PROJECT, dbParams,
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
