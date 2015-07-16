package org.webdroid.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Starter;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.webdroid.util.Log;

import java.lang.reflect.Array;

/**
 * 서버의 메인 클래스
 * Created by Micky Kim on 2015. 7. 17..
 */
public class ServerMain extends AbstractVerticle {

    private final static int WEB_PORT = 54321;

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

        Router router = initRouter();
        HttpServer server = vertx.createHttpServer();

        server.requestHandler(router::accept).listen(WEB_PORT);
    }

    /**
     * 라우팅 설정
     * @return 설정된 vertx 라우터
     */
    protected Router initRouter() {
        Router router = Router.router(vertx);

        // 페이지를 메모리에 캐싱해두는 것 같음
        // 페이지 업데이트시 항상 다시 켜야함
        router.route().handler(StaticHandler.create());
        return router;
    }
}
