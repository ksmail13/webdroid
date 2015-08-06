package org.webdroid.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.webdroid.constant.WebdroidServerConstant;

/**
 * Router for page route
 * Created by micky on 2015. 7. 26..
 */
public class WebdroidRouter {

    private final static boolean IS_CLUSTERRED = WebdroidServerConstant.Conf.IS_CLUSTERED;

    /**
     * generate webserver route
     * @param vertx vertx instance
     * @return new route
     */
    public static WebdroidRouter createRoute(Vertx vertx) {
        WebdroidRouter router = new WebdroidRouter(vertx);

        router.initBasicRouterhandler(vertx);
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
            res.sendFile(WebdroidServerConstant.Path.WEBROOT + WebdroidServerConstant.Path.STATIC + routingContext.normalisedPath());
        });
        
        // for image route
        router.route().pathRegex(IMG).handler(routingContext -> {
            HttpServerResponse res = routingContext.response();
            //res.putHeader("content-type", "image/"+routingContext.normalisedPath().split(".")[1]);
            //Log.logging("request image "+ routingContext.normalisedPath());

            res.sendFile(WebdroidServerConstant.Path.WEBROOT+ WebdroidServerConstant.Path.IMG_PATH+routingContext.normalisedPath());
        });
    }

    public void initBasicRouterhandler(Vertx vertx) {
        Route route = router.route();
        route.handler(CookieHandler.create());

        SessionStore ss = null;
        if(IS_CLUSTERRED) {
            ss = ClusteredSessionStore.create(vertx, WebdroidServerConstant.ID.SESSION_MAP_NAME);
        } else {
            ss = LocalSessionStore.create(vertx, WebdroidServerConstant.ID.SESSION_MAP_NAME);
        }

        route.handler(SessionHandler.create(ss));

        /**
         * for post request handling
         */
        route.handler(BodyHandler.create());

        /**
         * request logging
         */
        route.handler(LoggerHandler.create());

        /**
         * when error
         */
        route.failureHandler(ErrorHandler.create(true));
    }
}
