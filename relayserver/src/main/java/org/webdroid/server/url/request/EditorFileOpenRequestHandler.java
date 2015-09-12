package org.webdroid.server.url.request;

//import io.vertx.core.json.JsonObject;
//import org.webdroid.constant.HttpStatusCode;
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
 * Created by Seho on 2015-09-06.
 */
public class EditorFileOpenRequestHandler extends RequestHandler {

    public static final String URL = "/openfile";

    public EditorFileOpenRequestHandler(DBConnector dbConnector) {
        super(dbConnector, false, "filepath", "pid");
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {

        JsonArray dbParams = JsonUtil.createJsonArray(params.get("pid"));

        mDBConnector.query(Query.GET_PATH, dbParams, new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {
                JsonObject row = resultSet.getRows().get(0);

                res.setChunked(true);
                res.write(JqueryFileTree.openFileFromTree(req.getParam("filepath"),row.getString("p_path"))).end();
            }
        });

        //JsonObject obj = new JsonObject();
        //obj.put("innerText", JqueryFileTree.openFileFromTree(req.getParam("filepath")));
        //obj.put("ext",JqueryFileTree.fileExt(req.getParam("filepath")));
        //send(HttpStatusCode.SUCCESS, "application/json", obj.toString());
    }
}
