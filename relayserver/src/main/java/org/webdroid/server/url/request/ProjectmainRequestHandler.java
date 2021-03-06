package org.webdroid.server.url.request;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ��μ� on 2015-09-09.
 */
public class ProjectmainRequestHandler extends RequestHandler {

    public static final String URL = "/show_projectlist";

    public ProjectmainRequestHandler(DBConnector dbConnector) {
        super(dbConnector, true);
    }
    @Override

    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(session.get("id").toString());
        mDBConnector.query(Query.MY_PROJECT, dbParams,
                new SQLResultHandler<ResultSet>(this) {
                    @Override
                    public void success(ResultSet resultSet) {

                        List<JsonObject> resultList = resultSet.getRows();
                        List<JsonObject> filteredList = new ArrayList<>(resultList);

                        JsonObject row = new JsonObject();

                        resultList.removeIf(obj -> obj.getInteger("isImportant", 1) == 1);
                        row.put("projects", new JsonArray(resultList));

                        filteredList.removeIf(obj -> obj.getInteger("isImportant", 0) == 0);
                        row.put("favorates", new JsonArray(filteredList));

                        send(HttpStatusCode.SUCCESS, "application/json", row.toString());
                    }
                });
    }

}
