package org.webdroid.server.url.request;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import org.webdroid.constant.Query;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JqueryFileTree;
import org.webdroid.util.JsonUtil;

import java.util.Map;

/**
 * Created by Y on 2015-09-12.
 */
public class TreeBaseRequestHandler extends RequestHandler{

    public static final String URL = "/treebase";
    String path;

    public TreeBaseRequestHandler(DBConnector dbConnector) {super(dbConnector, false, "pid");
    }
    @Override
    public void handlingWithParams(Map<String, Object> params) {
        JsonArray dbParams = JsonUtil.createJsonArray(req.getParam("pid"));

        mDBConnector.query(Query.GET_PATH, dbParams , new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {
                JsonObject row = resultSet.getRows().get(0);
                path = row.getString("p_path");
                res.setChunked(true);
                res.write(path).end();
            }
        });
    }
}
