package org.webdroid.server.url.page;

import io.vertx.ext.web.templ.JadeTemplateEngine;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.server.handler.PageHandler;

/**
 * Created by micky on 2015. 9. 5..
 */
public class WelcomePageHandler extends PageHandler{

    public final static String URL = "/";

    public WelcomePageHandler() {
        super(null);
    }


    @Override
    public void handling() {
        if (isLogin()) {
            redirectTo("/projectmain");
            return;
        }

        rendering(JadeTemplateEngine.create(), WebdroidConstant.Path.HTML + "/welcome");
    }

}
