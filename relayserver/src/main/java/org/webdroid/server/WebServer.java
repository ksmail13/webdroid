package org.webdroid.server;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import org.webdroid.constant.*;
import org.webdroid.util.DBConnector;
import org.webdroid.util.JsonUtil;
import org.webdroid.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Webdroid Web Server
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

        mDBConnector = new DBConnector(vertx, aBoolean -> {
            if(aBoolean) {
                Log.logging("DB " + (aBoolean ? "Connected":"fail connect"));

                initRouter(server);
                server.listen(WEB_PORT);
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
        Router router = WebdroidRouter.createRoute(vertx).getRouter();


        requestHandling(router);
        pageRoute(router);
        server.requestHandler(router::accept);
    }

    private void pageRoute(Router router) {

        JadeTemplateEngine jadeTemplateEngine = JadeTemplateEngine.create();

        // main page
        router.route().path("/").handler(new RouteHandler() {
            @Override
            public void handling() {
                if (isLogin()) {
                    redirectTo("/projectmain");
                    return;
                }

                jadeTemplateEngine.render(context, WebdroidConstant.Path.HTML + "/welcome", renderRes -> {
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
            public void handling() {
                if (!isLogin()) {
                    redirectTo("/");
                    return;
                }
                context.put("name", session.get("name"));
                JsonArray params = JsonUtil.createJsonArray((Integer) session.get("id"));
                mDBConnector.query(Query.MY_PROJECT, params, queryResult -> {
                    ResultSet resultSet = queryResult.result();

                    List<JsonObject> resultList = resultSet.getRows();
                    List<JsonObject> filteredList = new ArrayList<>(resultList);

                    context.put("projects", resultList);

                    filteredList.removeIf(obj -> obj.getInteger("isImportant", 0) == 0);
                    context.put("favorates", filteredList);

                    jadeTemplateEngine.render(context, WebdroidConstant.Path.HTML + "/projectmain", renderRes -> {
                        if (renderRes.succeeded()) {
                            res.putHeader(HttpHeaders.CONTENT_TYPE, "text/html");
                            res.setStatusCode(200);

                            res.end(renderRes.result().toString("utf8"));
                        } else {
                            sendErrorResponse(500, renderRes.cause());
                        }
                    });

                });

            }
        });
    }


    /**
     * template resource routing also handling client req
     */
    public void requestHandling(Router router) {
        // sign in
        String[] signinParams = {"user_id", "user_pw"};
        router.post("/signin").handler(new RequestHandler(false, signinParams) {
            @Override
            public void reqRecvParams(Map<String, Object> params) {

                if(isLogin())
                    session.destroy();
                logger.debug(req.params().toString());
                JsonArray dbParams = JsonUtil.createJsonArray(params.get(signinParams[0]), params.get(signinParams[1]));

                if("true".equals(req.getParam("save_id"))) {
                    context.addCookie(Cookie.cookie("saveID", req.getParam("save_id")));
                    context.addCookie(Cookie.cookie("id", params.get("user_id").toString()));
                }

                mDBConnector.query(Query.SIGN_IN, dbParams, queryResult -> {
                    ResultSet resultSet = queryResult.result();

                    if (resultSet.getNumRows() > 0) {
                        JsonObject userInfo = resultSet.getRows().get(0);
                        logger.debug(userInfo.toString());
                        session.put("id", userInfo.getInteger("u_id"));
                        session.put("name", userInfo.getString("name"));


                        sendJsonResult(HttpStatusCode.SUCCESS, true,
                                ResultMessage.SIGNED_IN);
                    } else {
                        sendJsonResult(HttpStatusCode.SUCCESS, false,
                                ResultMessage.CHECK_ID_PW);
                    }
                });
            }
        });

        // sign up
        String[] signupParams = {"user_id", "user_name", "user_pw"};
        router.post("/signup").handler(new RequestHandler(false, signupParams) {
            @Override
            public void reqRecvParams(Map<String, Object> params) {
                JsonArray dbParams = JsonUtil.createJsonArray(params.get(signupParams[0]),
                        params.get(signupParams[1]),
                        params.get(signupParams[2]));

                mDBConnector.update(Query.SIGN_UP, dbParams, updateResult -> {
                    if (updateResult.succeeded()) {
                        UpdateResult result = updateResult.result();
                        if (result.getUpdated() > 0) {
                            sendJsonResult(200, true, ResultMessage.SIGNED_UP);
                        } else
                            sendJsonResult(200, false, ResultMessage.SINGED_UP_FAIL);
                    } else {
                        sendJsonResult(HttpStatusCode.RUNTIME_ERROR,
                                false, ResultMessage.INTERNAL_SERVER_ERROR);
                    }
                });
            }
        });

        router.route("/signout").handler(new RequestHandler(false) {
            @Override
            public void reqRecvParams(Map<String, Object> params) {
                session.destroy();
                context.clearUser();
                redirectTo("/");
                //sendJsonResult(HttpStatusCode.FOUND, true, "sign out");
            }
        });

        router.post("/createproject").handler(new RequestHandler(true, "project_name", "project_desc", "project_target_ver") {
            @Override
            public void reqRecvParams(Map<String, Object> params) {
                JsonArray dbParams = JsonUtil.createJsonArray(params.get("project_name"),
                        params.get("project_desc"), params.get("project_target_ver"));
                mDBConnector.update(Query.NEW_PROJECT, dbParams, updateResult -> {
                    if (updateResult.succeeded()) {
                        sendJsonResult(200, true, ResultMessage.SUCCESS);
                    } else {
                        sendErrorResponse(HttpStatusCode.RUNTIME_ERROR,
                                updateResult.cause());
                    }
                });
            }
        });
    }

    /**
     * simple route handler
     */
    private abstract class RouteHandler implements Handler<RoutingContext> {

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
         * res json createJsonResult
         * @param statusCode req createJsonResult code
         * @param result req createJsonResult
         * @param message req message
         */
        public final void sendJsonResult(int statusCode, boolean result, String message) {
            res.setStatusCode(statusCode);
            res.putHeader(HttpHeaders.CONTENT_TYPE, "text/json");
            res.end(JsonUtil.createJsonResult(result, message).toString());
        }

        public final void redirectTo(String url) {
            logger.debug("redirect to " +url);
            res.setStatusCode(HttpStatusCode.FOUND);
            res.putHeader("location", url);
            res.end();
        }

        public final void sendErrorResponse(int statusCode, Throwable cause) {
            res.setStatusCode(statusCode);
            res.putHeader(HttpHeaders.CONTENT_TYPE, "text/json");

            if(ServerConfigure.DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%d %s<br/><br/>",statusCode, cause.getMessage()));
                for (StackTraceElement ste : cause.getStackTrace()) {
                    sb.append(String.format("%s.%s(%s:%d)<br/>", ste.getClassName(), ste.getMethodName(), ste.getFileName(), ste.getLineNumber()));
                }

                res.end(JsonUtil.createJsonResult(false, sb.toString()).toString());
            }
            else {
                //TODO error page
                res.end(JsonUtil.createJsonResult(false, statusCode + " Error").toString());
            }
        }

        public final boolean isLogin() {
            return session.data().size() > 0;
        }

        /**
         * routing handling
         */
        public abstract void handling();
    }


    public abstract class RequestHandler extends RouteHandler {
        protected String[] requestParameters = null;
        protected boolean chkLogin = false;

        private RequestHandler() {}

        /**
         * Request handler not web page
         * @param checkLogin this request need auth
         * @param params parameter names.
         */
        public RequestHandler (boolean checkLogin, String... params) {
            chkLogin = checkLogin;
            requestParameters = params;
        }

        /**
         * Reqeust handler not web page
         * @param checkLogin this request need auth
         */
        public RequestHandler (boolean checkLogin) {
            chkLogin = checkLogin;
        }

        @Override
        public void handling() {
            if(chkLogin && !isLogin()) {
                sendJsonResult(401, false, "This operation will sign in");
                return ;
            }


            if(requestParameters == null) {
                reqRecvParams(null);
                return ;
            }

            HashMap<String, Object> params = new HashMap<>();
            // Check each parameters
            for (String requestParameter : requestParameters) {
                String receiveParam = req.getParam(requestParameter);
                if(receiveParam != null)
                    params.put(requestParameter, receiveParam);
                else {
                    reqRecvParamsLess(requestParameter);
                    return ;
                }
            }
            reqRecvParams(params);
        }

        /**
         * This method will execute when parameter is not enough
         */
        public void reqRecvParamsLess(String paramKey) {
            sendJsonResult(200, false, "you should include parameter "+paramKey);
        }

        /**
         * request handling
         * @param params parameters from client
         */
        public abstract void reqRecvParams(Map<String, Object> params);
    }
}
