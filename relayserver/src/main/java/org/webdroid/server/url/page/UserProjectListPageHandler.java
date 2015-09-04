package org.webdroid.server.url.page;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import org.webdroid.constant.Query;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.PageHandler;
import org.webdroid.util.JsonUtil;

import java.util.List;

/**
 * Created by micky on 2015. 9. 5..
 */
public class UserProjectListPageHandler extends PageHandler {

    public static final String URL = "/projectview";

    public UserProjectListPageHandler(DBConnector dbConnector) {
        super(dbConnector);
    }

    @Override
    public void handling() {
        if (!isLogin()) {
            redirectTo("/");
            return;
        }

        context.put("name", session.get("name"));
        JsonArray params = JsonUtil.createJsonArray((Integer) session.get("id"));

        mDBConnector.query(Query.MY_PROJECT, params, new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {
                List<JsonObject> resultList = resultSet.getRows();

                context.put("projects", resultList);

                rendering(templateEngine, WebdroidConstant.Path.HTML + "/projectview");
            }
        });
    }
}
