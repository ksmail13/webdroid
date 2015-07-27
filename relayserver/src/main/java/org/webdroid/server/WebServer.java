package org.webdroid.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import org.webdroid.util.DBConnector;
import org.webdroid.util.Log;

/**
 * Created by micky on 2015. 7. 27..
 */
public class WebServer extends AbstractVerticle {

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

        server.requestHandler(WebdroidRouter.createRoute(vertx).getRouter()::accept);
    }


}
