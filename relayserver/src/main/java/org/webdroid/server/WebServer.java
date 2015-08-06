package org.webdroid.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.webdroid.constant.WebdroidServerConstant;
import org.webdroid.util.DBConnector;
import org.webdroid.util.Log;

/**
 * Created by micky on 2015. 7. 27..
 */
public class WebServer extends WebdroidVerticle {

    private final static int WEB_PORT = 54321;
    private DBConnector mDBConnector = null;

    /**
     * 서버 시작지점
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        Log.logging("Begin server");

        HttpServer server = vertx.createHttpServer();
        initRouter(server);
        server.listen(WEB_PORT);

        mDBConnector = new DBConnector(vertx, aBoolean -> Log.logging("DB " + (aBoolean ? "Connected":"fail connect")));

    }

    /**
     * vert.x instance가 종료 될 때 실행되는 메서드
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        mDBConnector.close();
        Log.logging("stop server");
    }

    /**
     * 라우팅 설정
     * @param server 라우팅할 서버 객체
     */
    protected void initRouter(HttpServer server) {
        Router router = WebdroidRouter.createRoute(vertx).getRouter();
        server.requestHandler(router::accept);

        requestHandling(router);
    }


    /**
     * template resource routing also handling client request
     */
    public void requestHandling(Router router) {

        // main page
        router.route().path("/").handler(routingContext -> {
            HttpServerResponse res = routingContext.response();
            res.putHeader("content-type", "text/html");
            Log.logging("request page "+routingContext.normalisedPath());

            res.sendFile(WebdroidServerConstant.Path.STATIC + "/welcome.html");
        });

        // sign in
        router.route().path("/signin").handler(new RouteHandler() {
            @Override
            public void handling(Session session, HttpServerRequest req, HttpServerResponse res) {
                String id = req.getParam("user_id");
                String pw = req.getParam("user_pw");


            }
        });
    }

    /**
     * route handler simple
     */
    private abstract class RouteHandler implements Handler<RoutingContext> {

        @Override
        public void handle(RoutingContext routingContext) {
            handling(routingContext.session(), routingContext.request(), routingContext.response());
        }

        /**
         * routing handling
         * @param session
         * @param req
         * @param res
         */
        public abstract void handling(Session session, HttpServerRequest req, HttpServerResponse res);
    }
}
