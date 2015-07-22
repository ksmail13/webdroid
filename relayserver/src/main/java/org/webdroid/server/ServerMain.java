package org.webdroid.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Starter;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.webdroid.util.Log;

/**
 * 서버의 메인 클래스
 * Created by Micky Kim on 2015. 7. 17..
 */
public class ServerMain extends AbstractVerticle {

    private final static int WEB_PORT = 54321;
    private final static String DB_IP = "192.168.0.48";
    private final static String DB_PORT = "3306";
    private final static String DB_NAME = "webdroid";
    private final static String DB_ID = DB_NAME;
    private final static String DB_PW = "web321droid!@#";

    private JsonObject mDBConnectionConfig = null;
    private JDBCClient mJDBCClient = null;
    private SQLConnection mDBConnection = null;


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

        mDBConnectionConfig = initDBConfig();

        mJDBCClient = JDBCClient.createShared(vertx, mDBConnectionConfig, DB_NAME);

        mJDBCClient.getConnection(handler -> {
            Log.logging("DB ON`");
            mDBConnection = handler.result();
        });
    }

    /**
     * vert.x instance가 종료 될 때 실행되는 메서드
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();

        Log.logging("stop server");
    }

    /**
     * 라우팅 설정
     * @param server 라우팅할 서버 객체
     */
    protected void initRouter(HttpServer server) {
        Router router = Router.router(vertx);

        // static web page router
        router.route().handler(StaticHandler.create());

        server.requestHandler(router::accept);
    }


    /*
    DB 설정
    address  : "test.persistor",

  // JDBC connection settings
  driver   : "com.mysql.jdbc.Driver",
  url      : "jdbc:mysql://localhost:3306/vertx",
  username : "test",
  password : "test",
     */

    /**
     * DB접속을 위한 설정값을 생성한다.
     * @return 설정된 DB접속 정보
     */
    protected JsonObject initDBConfig() {
        JsonObject config = new JsonObject();
        config.put("address", "webdroid.persistor");
        config.put("driver", "com.mysql.jdbc.Driver");
        config.put("url", String.format("jdbc:mysql://%s:%s/%s", DB_IP, DB_PORT, DB_NAME));
        config.put("username", DB_NAME);
        config.put("password", DB_PW);

        return config;
    }

}
