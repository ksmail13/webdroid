package org.webdroid.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Starter;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.webdroid.util.DBConnector;
import org.webdroid.util.Log;

/**
 * 서버의 메인 클래스
 * Created by Micky Kim on 2015. 7. 17..
 */
public class ServerMain extends AbstractVerticle {

    private final static int WEB_PORT = 54321;
    private DBConnector mDBConnector = null;

    /**
     * 서버 실행 엔트리 포인트
     * @param args 서버 실행에 사용할 아규먼트
     */
    public static void main(String[] args) {
        String[] basicArgs  = {"run", ServerMain.class.getName()};
        String[] serverArgs = new String[basicArgs.length + args.length];

        System.arraycopy(basicArgs, 0, serverArgs, 0, 2);
        if(args.length > 0)
            System.arraycopy(args, 0, serverArgs, 2, args.length);

        Starter.main(serverArgs);
    }

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

        mDBConnector = new DBConnector(vertx, aBoolean -> {
            Log.logging("DB " + (aBoolean ? "Connected":"Unconnected"));
            if(aBoolean) {
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
        Log.logging("stop server");
    }

    /**
     * 라우팅 설정
     * @param server 라우팅할 서버 객체
     */
    protected void initRouter(HttpServer server) {
        Router router = Router.router(vertx);

        // static web page router
        router.route("/static/*").handler(StaticHandler.create());

        server.requestHandler(router::accept);
    }


}
