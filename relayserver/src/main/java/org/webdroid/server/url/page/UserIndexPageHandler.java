package org.webdroid.server.url.page;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.PageHandler;
import org.webdroid.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micky on 2015. 9. 5..
 */
public class UserIndexPageHandler extends PageHandler {

    public static final String URL = "/projectmain";

    public UserIndexPageHandler(DBConnector dbConnector) {
        super(dbConnector);
    }

    @Override
    public void handling() {
        /*if (!isLogin()) {
            redirectTo("/");
            return;
        }
        context.put("name", session.get("name")); //jade에서 projectmain 에서 쓰임
        JsonArray params = JsonUtil.createJsonArray((Integer) session.get("id"));

        mDBConnector.query(Query.MY_PROJECT, params, new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {
                List<JsonObject> resultList = resultSet.getRows();
                List<JsonObject> filteredList = new ArrayList<>(resultList);

                context.put("projects", resultList);

                filteredList.removeIf(obj -> obj.getInteger("isImportant", 0) == 0);
                context.put("favorates", filteredList);
                rendering(JadeTemplateEngine.create(), WebdroidConstant.Path.HTML + "/projectmain");  //jade 변환한 파일이름
            }
        });*/
        if (!isLogin()) {
            redirectTo("/");
            return;
        }
        context.put("name", session.get("name"));
        JsonArray params = JsonUtil.createJsonArray((Integer) session.get("id"));

        mDBConnector.query(Query.USER_ALL_INFOPROFILE, params, new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {
                JsonObject row = resultSet.getRows().get(0);

                String[] keys = {"id", "git_id", "introduce", "user_img"};
                String[] errorDefault = {ResultMessage.ID_ERROR, ResultMessage.GIT_ERROR, ResultMessage.INTRODUCE_ERROR, ""};

                for (int i = 0; i < keys.length; i++) {
                    String data = row.getString(keys[i]);
                    logger.debug(data);
                    if (data == null) {
                        context.put("show_"+keys[i], errorDefault[i]);
                    } else {
                        context.put("show_"+keys[i], data);
                    }
                }
                rendering(JadeTemplateEngine.create(), WebdroidConstant.Path.HTML + "/profile");  //jade변환한 파일이름
            }
        });
    }
}
