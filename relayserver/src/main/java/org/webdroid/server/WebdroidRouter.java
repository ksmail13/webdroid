package org.webdroid.server;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.webdroid.constant.WebdroidConstant;

/**
 * Router for page route
 * Created by micky on 2015. 7. 26..
 */
public class WebdroidRouter {

    private final static boolean IS_CLUSTERRED = WebdroidConstant.Conf.IS_CLUSTERED;

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
        final String BOOTSTRAP = "/bootstrap/(css|fonts|js)/\\S+.\\S+";
        final String CSS = "/css/\\S+.(css)";
        final String JS = "/js/\\S+.(js)";
        final String IMG = "/images/\\S+.(jpeg|png|jpg|ico)";

        StaticHandler staticHandler = StaticHandler.create(WebdroidConstant.Path.STATIC);

        router.route().pathRegex(BOOTSTRAP).handler(staticHandler);
        router.route().pathRegex(JS).handler(staticHandler);
        router.route().pathRegex(CSS).handler(staticHandler);
        router.route().pathRegex(IMG).handler(staticHandler);


    }

    public void initBasicRouterhandler(Vertx vertx) {

        /**
         * request logging
         */
        router.route().handler(LoggerHandler.create());

        /**
         * when error
         */
        router.route().failureHandler(ErrorHandler.create(true));


        /**
         * for post request handling
         */
        router.route().handler(BodyHandler.create());


        router.route().handler(CookieHandler.create());

        SessionStore ss = null;
        if(IS_CLUSTERRED) {
            ss = ClusteredSessionStore.create(vertx, WebdroidConstant.ID.SESSION_MAP_NAME);
        } else {
            ss = LocalSessionStore.create(vertx, WebdroidConstant.ID.SESSION_MAP_NAME);
        }

        router.route().handler(SessionHandler.create(ss));
    }
}
