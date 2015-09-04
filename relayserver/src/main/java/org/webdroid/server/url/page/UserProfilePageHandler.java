package org.webdroid.server.url.page;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.PageHandler;
import org.webdroid.util.JsonUtil;

/**
 * Created by micky on 2015. 9. 5..
 */
public class UserProfilePageHandler extends PageHandler{

    public static final String URL = "/profile";

    public UserProfilePageHandler(DBConnector dbConnector) {
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

        mDBConnector.query(Query.USER_ALL_INFOPROFILE, params, new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {
                JsonObject row = resultSet.getRows().get(0);
                if (row.getString("id") == null) {
                    context.put("show_id", ResultMessage.ID_ERROR);
                } else {
                    context.put("show_id", row.getString("id"));
                }

                if (row.getString("git_id") == null) {
                    context.put("show_git_id", ResultMessage.GIT_ERROR);
                } else {
                    context.put("show_git_id", row.getString("git_id"));
                }

                if (row.getString("introduce") == null) {
                    context.put("show_introduce", ResultMessage.INTRODUCE_ERROR);
                } else {
                    context.put("show_introduce", row.getString("introduce"));
                }
                rendering(JadeTemplateEngine.create(), WebdroidConstant.Path.HTML + "/profile");  //jade변환한 파일이름
            }
        });
    }
}
