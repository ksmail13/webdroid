package org.webdroid.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import org.webdroid.util.Log;

/**
 * Created by 김민수 on 2015-07-27.
 */
public class SocketJSServer extends AbstractVerticle {

    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        //initRouter(server);


        /*mDBConnector = new DBConnector(vertx, aBoolean -> {
            Log.logging("DB " + (aBoolean ? "Connected":"Unconnected"));
            if(aBoolean) {
            }
        });*/

        Router router =  Router.router(vertx);
        SockJSHandlerOptions Options = new SockJSHandlerOptions().setHeartbeatInterval(2000);
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx,Options);

        sockJSHandler.socketHandler(sockJSSocket -> {
            Log.logging(sockJSSocket.remoteAddress().host());
            sockJSSocket.handler(buffer -> {

                sockJSSocket.write(buffer);

            });
        });
        router.route("/myapp").handler(routingContext -> {
            routingContext.response().sendFile("./webroot/static/12.html").end();
        });

        router.route("/myapp/*").handler(sockJSHandler);
        router.route("/sockjs-0.3.4.js").handler(routingContext -> {
            routingContext.response().sendFile("./webroot/static/sockjs-0.3.4.js").end();
        });
        server.requestHandler(router::accept);
        server.listen(8080);
    }
}
