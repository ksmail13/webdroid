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
public class CreateProjectRequestHandler extends RequestHandler {
    public static final String URL = "/createproject";

    static String[] createprojectParams= {"project_name", "project_desc", "project_target_ver"};

    public CreateProjectRequestHandler(DBConnector dbConnector) {
        super(dbConnector, true, createprojectParams);
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(params.get("project_name"),
                params.get("project_desc"), session.get("id"));
        mDBConnector.update(Query.NEW_PROJECT, dbParams,
                new SQLResultHandler<UpdateResult>(this) {
                    @Override
                    public void success(UpdateResult resultSet) {
                        sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.SUCCESS);
                    }
                });
    }
}
