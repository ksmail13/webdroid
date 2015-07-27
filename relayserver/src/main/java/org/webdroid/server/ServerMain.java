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

    public void start() {
        vertx.deployVerticle(WebServer.class.getName());
    }
}
