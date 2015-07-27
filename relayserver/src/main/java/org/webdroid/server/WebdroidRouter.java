package org.webdroid.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.webdroid.util.Log;

/**
 * Router for page route
 * Created by micky on 2015. 7. 26..
 */
public class WebdroidRouter {

    /**
     * resource path
     */
    private final static String WEBROOT = "./webroot";

    /**
     * static resource path
     */
    private final static String STATIC = "/static";

    /**
     * image resource path
     */
    private final static String IMG_PATH = STATIC+"/images";

    /**
     * generate webserver route
     * @param vertx vertx instance
     * @return new route
     */
    public static WebdroidRouter createRoute(Vertx vertx) {
        WebdroidRouter router = new WebdroidRouter(vertx);

        router.initDynamicPage();
        router.initStaticResource();

        return router;
    }

    private Router router = null;

    /**
     * create new router
     * @param vertx
     */
    private WebdroidRouter(Vertx vertx)
    {
        router = Router.router(vertx);
    }

    /**
     * get router object
     * @return router
     */
    public Router getRouter() {
        return router;
    }

    /**
     * static resource routing
     */
    public void initStaticResource() {
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

    /**
     * template resource routing also handling client request
     */
    public void initDynamicPage() {
        router.route().path("/").handler(routingContext -> {
            HttpServerResponse res = routingContext.response();
            res.putHeader("content-type", "text/html");
            Log.logging("request page "+routingContext.normalisedPath());

            res.sendFile(WEBROOT +  STATIC + "/welcome.html").end();
        });
    }

}
