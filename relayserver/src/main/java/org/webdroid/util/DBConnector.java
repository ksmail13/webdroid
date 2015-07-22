package org.webdroid.util;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.Optional;

/**
 * Created by micky on 2015. 7. 22..
 */
public class DBConnector {
    private static DBConnector instance = null;


    //private final static String DB_IP = "192.168.0.48";
    private final static String DB_IP = "10.0.26.220";
    private final static String DB_PORT = "3306";
    private final static String DB_NAME = "webdroid";
    private final static String DB_ID = "webdroid";
    private final static String DB_PW = "web321droid!@#";

    private JsonObject mDBConnectionConfig = null;
    private Optional<JDBCClient> mJDBCClient = null;
    private Optional<SQLConnection> mSqlCommand = null;

    private boolean isConnect = false;

    public DBConnector(Vertx vertx, Handler<Boolean> callback) {
        mDBConnectionConfig = initDBConfig();

        mJDBCClient = Optional.ofNullable(JDBCClient.createShared(vertx, mDBConnectionConfig, DB_NAME));

        mJDBCClient.ifPresent(jdbcClient -> {
            jdbcClient.getConnection(handler -> {
                Log.logging("DB connect");
                isConnect = true;
                mSqlCommand = Optional.ofNullable(handler.result());
                callback.handle(isConnect);
            });
        });
    }

    public void close() {
        mSqlCommand.ifPresent(sqlConnection -> sqlConnection.close());
        mJDBCClient.ifPresent(jdbcClient -> jdbcClient.close());
    }

    public void insert(JsonObject data) {
        mSqlCommand.ifPresent(sqlConnection -> {
            // TODO : insert
        });
    }

    public void select(JsonObject data) {
        mSqlCommand.ifPresent(sqlConnection -> {
            // TODO : select
        });
    }

    public void update(JsonObject data) {
        mSqlCommand.ifPresent(sqlConnection -> {
            // TODO : update
        });
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
        config.put("user", DB_ID);
        config.put("password", DB_PW);

        return config;
    }
}
