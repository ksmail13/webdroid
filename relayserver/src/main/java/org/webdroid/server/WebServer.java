package org.webdroid.server;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.templ.MVELTemplateEngine;
import org.webdroid.constant.Query;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.util.DBConnector;
import org.webdroid.util.JsonUtil;
import org.webdroid.util.Log;
import org.webdroid.util.RequestResult;

import java.util.ArrayList;
import java.util.List;

/**
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
        Router router = WebdroidRouter.createRoute(vertx).getRouter();


        requestHandling(router);

        server.requestHandler(router::accept);
    }


    /**
     * template resource routing also handling client request
     */
    public void requestHandling(Router router) {
        MVELTemplateEngine templEngine = MVELTemplateEngine.create();
        templEngine.setExtension(".html");


        // main page
        router.route().path("/").handler(new RouteHandler() {
            @Override
            public void handling(RoutingContext context, Session session, HttpServerRequest req, HttpServerResponse res) {
                if(session.get("id") != null) {
                    redirectTo("/projectmain");
                    return;
                }


                templEngine.render(context, WebdroidConstant.Path.HTML + "/welcome", renderRes -> {
                    if (renderRes.succeeded()) {
                        res.putHeader(HttpHeaders.CONTENT_TYPE, "text/html");
                        res.setStatusCode(200);
                        res.end(renderRes.result().toString());
                    } else {
                        sendErrorResponse(500, renderRes.cause());
                    }
                });
            }
        });

        // main page
        router.route().path("/projectmain").handler(new RouteHandler() {
            @Override
            public void handling(RoutingContext context, Session session, HttpServerRequest req, HttpServerResponse res) {
                if (session.get("id") == null) {
                    redirectTo("/");
                    return;
                }
                context.put("name", session.get("name"));
                JsonArray params = JsonUtil.createJsonArray((Integer)session.get("id"));
                mDBConnector.query(Query.MY_PROJECT, params, resultSet -> {
                    List<JsonObject> resultList = resultSet.getRows();
                    List<JsonObject> filteredList = new ArrayList<JsonObject>(resultList);

                    context.put("projects", resultList);

                    filteredList.removeIf(obj -> obj.getInteger("isImportant", 0) == 0);
                    context.put("favorates", filteredList);

                    templEngine.render(context, WebdroidConstant.Path.HTML + "/projectmain", renderRes -> {
                        if (renderRes.succeeded()) {
                            res.putHeader(HttpHeaders.CONTENT_TYPE, "text/html");
                            res.setStatusCode(200);
                            res.end(renderRes.result().toString());
                        } else {
                            sendErrorResponse(500, renderRes.cause());
                        }
                    });

                }, error -> {
                });

            }
        });

        // sign in
        router.post("/signin").handler(new RouteHandler() {
            @Override
            public void handling(RoutingContext context, Session session, HttpServerRequest req, HttpServerResponse res) {
                String id = req.getFormAttribute("user-id");
                String pw = req.getParam("user-pw");


                if (id == null || pw == null) {
                    sendJsonResult(200, false, "no parameter");
                    return;
                }

                JsonArray params = new JsonArray().add(id).add(pw);
                mDBConnector.query(Query.SIGN_IN, params, resultSet -> {
                    if (resultSet.getNumRows() > 0) {
                        JsonObject userInfo = resultSet.getRows().get(0);
                        logger.debug(userInfo.toString());
                        session.put("id", userInfo.getInteger("u_id"));
                        session.put("name", userInfo.getString("name"));

                        sendJsonResult(WebdroidConstant.StatusCode.SUCCESS, true, WebdroidConstant.Message.SIGNED_IN);
                    } else {
                        sendJsonResult(WebdroidConstant.StatusCode.SUCCESS, false, WebdroidConstant.Message.CHECK_ID_PW);
                    }
                }, error -> sendJsonResult(WebdroidConstant.StatusCode.RUNTIME_ERROR, false, error.getMessage()));
            }
        });

        // sign up
        router.post("/signup").handler(new RouteHandler() {
            @Override
            public void handling(RoutingContext context, Session session, HttpServerRequest req, HttpServerResponse res) {
                String id = req.getParam("user_id");
                String name = req.getParam("user_name");
                String password = req.getParam("user_pw");

                JsonArray params = new JsonArray().add(id).add(password).add(name);
                mDBConnector.update(Query.SIGN_UP, params, updateResult -> {
                    if (updateResult.succeeded()) {
                        UpdateResult result = updateResult.result();
                        if(result.getUpdated() > 0) {
                            sendJsonResult(200, true, WebdroidConstant.Message.SIGNED_UP);
                        }
                        else
                            sendJsonResult(200, false, WebdroidConstant.Message.SINGED_UP_FAIL);
                    }
                    else {
                        sendJsonResult(WebdroidConstant.StatusCode.RUNTIME_ERROR, false, updateResult.cause().getMessage());
                    }
                });
            }
        });
    }

    /**
     * simple route handler
     */
    private abstract class RouteHandler implements Handler<RoutingContext> {

        private HttpServerResponse response;

        @Override
        public void handle(RoutingContext routingContext) {
            response = routingContext.response();
            // set character set utf-8
            response.putHeader(HttpHeaders.ACCEPT_CHARSET, WebdroidConstant.Conf.SERVER_ENCODING);
            handling(routingContext, routingContext.session(), routingContext.request(), routingContext.response());
        }

        /**
         * response json result
         * @param statusCode request result code
         * @param result request result
         * @param message request message
         */
        public final void sendJsonResult(int statusCode, boolean result, String message) {
            response.setStatusCode(statusCode);
            response.putHeader(HttpHeaders.CONTENT_TYPE, "text/json");
            response.end(RequestResult.result(result, message).toString());
        }

        public final void redirectTo(String url) {
            response.setStatusCode(302);
            response.putHeader("location", url);
            response.end();
        }

        public final void sendErrorResponse(int statusCode, Throwable cause) {
            response.setStatusCode(statusCode);
            response.putHeader(HttpHeaders.CONTENT_TYPE, "text/html");
            if(WebdroidConstant.Conf.DEBUG) {
                StringBuffer sb = new StringBuffer();
                sb.append(statusCode + " " + cause.getMessage() + "<br/><br/>");
                for (StackTraceElement ste : cause.getStackTrace()) {
                    sb.append(String.format("%s.%s(%s:%d)<br/>", ste.getClassName(), ste.getMethodName(), ste.getFileName(), ste.getLineNumber()));
                }

                response.end(sb.toString());
            }
            else {
                //TODO error page
                response.end(statusCode + " Error");
            }
        }

        /**
         * routing handling
         * @param context
         * @param session
         * @param req
         * @param res
         */
        public abstract void handling(RoutingContext context, Session session, HttpServerRequest req, HttpServerResponse res);
    }
}
