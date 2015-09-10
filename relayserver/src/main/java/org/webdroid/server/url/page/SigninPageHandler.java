package org.webdroid.server.url.page;

import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.server.handler.PageHandler;

/**
 * Created by micky on 2015. 9. 5..
 */
public class SigninPageHandler extends PageHandler{

    public static final String URL = "/signin_original";

    public SigninPageHandler() {
        super(null);
    }

    @Override
    public void handling() {

        rendering(templateEngine, WebdroidConstant.Path.HTML + "/signin_original");
    }
}
