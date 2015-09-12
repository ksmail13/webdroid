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
public class GitIdUpdateRequestHandler extends RequestHandler {

    public static final String URL = "/gitsubmit";

    public GitIdUpdateRequestHandler(DBConnector dbConnector) {
        super(dbConnector, true, "git_id");
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        System.out.println("gitsubmit");
        JsonArray dbParams = JsonUtil.createJsonArray(params.get("git_id"), session.get("id"));
        mDBConnector.update(Query.NEW_GIT, dbParams,
                new SQLResultHandler<UpdateResult>(this) {
                    @Override
                    public void success(UpdateResult resultSet) {
                        //sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.SUCCESS);
                        if (resultSet.getUpdated() > 0) {
                            sendJsonResult(200, true, ResultMessage.SUCCESS);
                            //redirectTo("/");
                        } else
                            sendJsonResult(200, false, ResultMessage.INTRODUCE_ERROR);

                    }
                });
    }
}
