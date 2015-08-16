package org.webdroid.db;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.ResultMessage;
import org.webdroid.server.handler.RouteHandler;
import org.webdroid.util.ConsoleLogger;

/**
 * Created by 민규 on 2015-08-17.
 */
public abstract class SQLResultHandler<T> implements Handler<AsyncResult<T>> {

    ConsoleLogger logger = ConsoleLogger.createLogger(getClass());

    private RouteHandler routeHandler;

    public SQLResultHandler(RouteHandler routeHandler) {
        this.routeHandler = routeHandler;
    }

    @Override
    public void handle(AsyncResult<T> resultSetAsyncResult) {
        if(resultSetAsyncResult.succeeded()) {
            success(resultSetAsyncResult.result());
        } else {
            failed(resultSetAsyncResult.cause());
        }
    }

    /**
     * when execute query success
     * @param resultSet
     */
    public abstract void success(T resultSet);

    /**
     * when query failed
     * @param t
     */
    public void failed(Throwable t) {
        logger.error("sql error", t);
        routeHandler.send(HttpStatusCode.RUNTIME_ERROR, "text/plain", ResultMessage.INTERNAL_SERVER_ERROR);
    }

}
