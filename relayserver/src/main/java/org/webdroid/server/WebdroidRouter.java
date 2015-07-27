package org.webdroid.server;

import io.netty.handler.codec.http.HttpResponse;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import org.webdroid.util.Log;

/**
 * 페이지 이동 라우터
 * Created by micky on 2015. 7. 26..
 */
public class WebdroidRouter {
    private Router router = null;
    private final static String WEBROOT = "./webroot";
    private final static String STATIC = "/static";
    private final static String IMG_PATH = STATIC+"/images";
    public static WebdroidRouter createRoute(Vertx vertx) {
        WebdroidRouter router = new WebdroidRouter(vertx);

        router.initDynamicPage();
        router.initStaticPage();

        return router;
    }

    private WebdroidRouter(Vertx vertx)
    {
        router = Router.router(vertx);
    }

    public Router getRouter() {
        return router;
    }

    public void initStaticPage() {
        final String CSS_JS = "/\\S+.(css|js)";
        final String IMG = "/\\S+.(jpeg|png|jpg|ico)";
        // for javascript and style sheet route
        router.route().pathRegex(CSS_JS).handler(routingContext -> {
            HttpServerResponse res = routingContext.response();

            res.sendFile(WEBROOT + STATIC + routingContext.normalisedPath());
        });

        // for image route
        router.route().pathRegex(IMG).handler(routingContext -> {
            HttpServerResponse res = routingContext.response();
            res.putHeader("content-type", "image/"+routingContext.normalisedPath().split(".")[1]);
            Log.logging("request image "+ routingContext.normalisedPath());

            res.sendFile(WEBROOT+IMG_PATH+routingContext.normalisedPath());
        });
    }

    public void initDynamicPage() {
        router.route().path("/").handler(routingContext -> {
            HttpServerResponse res = routingContext.response();
            res.putHeader("content-type", "text/html");
            Log.logging("request page "+routingContext.normalisedPath());

            res.sendFile(WEBROOT +  STATIC + "/welcome.html").end();
        });
    }

    public void initRequest

}
