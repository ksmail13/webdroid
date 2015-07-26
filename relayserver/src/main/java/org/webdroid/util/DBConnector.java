package org.webdroid.util;

import com.sun.istack.internal.Nullable;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.Optional;

/**
 * Created by micky on 2015. 7. 22..
 */
public class DBConnector {
    private static DBConnector instance = null;


    //private final static String DB_IP = "192.168.0.48";
    private final static String DB_IP = "211.189.19.39";
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
        mSqlCommand.ifPresent(SQLConnection::close);
        mJDBCClient.ifPresent(JDBCClient::close);
        isConnect = false;
    }

    public void query(String query, Handler<ResultSet> success, @Nullable Handler<Throwable> error) {
        mSqlCommand.ifPresent(sqlConnection -> {
            Log.logging("query "+query);
            sqlConnection.query(query, resultSetAsyncResult -> {
                if (resultSetAsyncResult.succeeded()) {
                    success.handle(resultSetAsyncResult.result());
                } else {
                    if (error != null)
                        error.handle(resultSetAsyncResult.cause());
                    else
                        resultSetAsyncResult.cause().printStackTrace();
                }
            });
        });

    }

    public void queryWithParam(String query, JsonArray params, Handler<ResultSet> success, @Nullable Handler<Throwable> error) {
        mSqlCommand.ifPresent(sqlConnection -> {
            Log.logging("queryWithParam "+query);
            sqlConnection.queryWithParams(query, params, resultSetAsyncResult -> {
                if (resultSetAsyncResult.succeeded()) {
                    success.handle(resultSetAsyncResult.result());
                } else {
                    if(error != null)
                        error.handle(resultSetAsyncResult.cause());
                    else
                        resultSetAsyncResult.cause().printStackTrace();
                }
            });
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
