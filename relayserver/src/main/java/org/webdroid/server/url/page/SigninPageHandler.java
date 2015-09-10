package org.webdroid.server.url.page;

import io.vertx.core.Vertx;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.server.handler.PageHandler;

/**
 * Created by micky on 2015. 9. 5..
 */
public class SigninPageHandler extends PageHandler{

    public static final String URL = "/signin_original";
    Vertx vertx;
    public SigninPageHandler(Vertx vertx) {
        super(null);
        this.vertx = vertx;
    }

    @Override
    public void handling() {

        //rendering(templateEngine, WebdroidConstant.Path.HTML + "/signin_original");
        vertx.fileSystem().readFile(WebdroidConstant.Path.TEMPL_HTML + "/signin_original.html", res -> {
            if(res.succeeded())
                send(HttpStatusCode.SUCCESS, "text/html", res.result());

            else {
                send(HttpStatusCode.RUNTIME_ERROR, "text/plain", res.cause().getMessage());
            }
        });

    }
}
