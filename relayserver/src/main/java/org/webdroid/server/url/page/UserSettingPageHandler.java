package org.webdroid.server.url.page;

import io.vertx.core.json.JsonArray;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.server.handler.PageHandler;
import org.webdroid.util.JsonUtil;

/**
 * Created by micky on 2015. 9. 5..
 */
public class UserSettingPageHandler extends PageHandler{


    public static final String URL = "/setting";

    public UserSettingPageHandler() {
        super(null);
    }

    @Override
    public void handling() {

        context.put("name", session.get("name")); //jade에서 projectmain 에서 쓰임
        JsonArray params = JsonUtil.createJsonArray((Integer) session.get("id"));
        rendering(templateEngine, WebdroidConstant.Path.HTML + "/setting");
    }
}
