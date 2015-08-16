package org.webdroid.server.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.ServerConfigure;
import org.webdroid.util.ConsoleLogger;
import org.webdroid.util.JsonUtil;

/**
 * simple route handler
 */
public abstract class RouteHandler implements Handler<RoutingContext> {
    protected ConsoleLogger logger = ConsoleLogger.createLogger(getClass());
    protected HttpServerRequest req;
    protected HttpServerResponse res;
    protected Session session;
    protected RoutingContext context = null;

    @Override
    public void handle(RoutingContext routingContext) {
        this.context = routingContext;
        res = routingContext.response();
        session = routingContext.session();
        req = routingContext.request();
        // set character set utf-8
        res.putHeader(HttpHeaders.ACCEPT_CHARSET, ServerConfigure.SERVER_ENCODING);
        handling();
    }

    /**
     * send response
     * @param statusCode status code
     * @param contentType data type (eg. html, json)
     * @param data data
     */
    public final void send(int statusCode, String contentType, String data) {
        res.setStatusCode(statusCode);
        res.putHeader(HttpHeaders.CONTENT_TYPE, contentType);
        res.end(data);
    }

    /**
     * res json createJsonResult
     * @param statusCode req createJsonResult code
     * @param result req createJsonResult
     * @param message req message
     */
    public final void sendJsonResult(int statusCode, boolean result, String message) {
        send(statusCode, "application/json", JsonUtil.createJsonResult(result, message).toString());
    }

    public final void redirectTo(String url) {
        logger.debug("redirect to " +url);
        res.setStatusCode(HttpStatusCode.FOUND);
        res.putHeader("location", url);
        res.end();
    }

    public final boolean isLogin() {
        return session.data().size() > 0;
    }

    /**
     * routing handling
     */
    public abstract void handling();
}