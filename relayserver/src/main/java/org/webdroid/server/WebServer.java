package org.webdroid.server;

import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.webdroid.db.DBConnector;
import org.webdroid.util.Log;

/**
 * Webdroid Web Server
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

        mDBConnector = new DBConnector(vertx, aBoolean -> {
            if(aBoolean) {
                Log.logging("DB " + (aBoolean ? "Connected":"fail connect"));

                initRouter(server);
                server.listen(WEB_PORT);
            }

        });
    }

    /**
     * vert.x instance가 종료 될 때 실행되는 메서드
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        mDBConnector.close();
        logger.info("stop server");
    }

    /**
     * 라우팅 설정
     * @param server 라우팅할 서버 객체
     */
    protected void initRouter(HttpServer server) {
        Router router = WebdroidRouterFactory.createRouter(vertx, mDBConnector);
        server.requestHandler(router::accept);
    }
}
