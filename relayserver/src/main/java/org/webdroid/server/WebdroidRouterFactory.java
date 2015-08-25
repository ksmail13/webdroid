package org.webdroid.server;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import org.webdroid.constant.*;
import org.webdroid.server.handler.PageHandler;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.db.DBConnector;
import org.webdroid.util.JsonUtil;
import org.webdroid.db.SQLResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Router for page route
 * Created by micky on 2015. 7. 26..
 */
public class WebdroidRouterFactory {

    private final static boolean IS_CLUSTERRED = ServerConfigure.IS_CLUSTERED;

    private DBConnector mDBConnector;

    /**
     * generate webserver route
     * @param vertx vertx instance
     * @return new route
     */
    public static Router createRouter(Vertx vertx, DBConnector dbConnector) {
        WebdroidRouterFactory router = new WebdroidRouterFactory(vertx, dbConnector);
        router.initBasicRouterhandler(vertx);
        router.initStaticResource();
        router.pageRoute();
        router.requestHandling();

        return router.getRouter();
    }

    private Router router = null;

    /**
     * create new router
     * @param vertx
     */
    private WebdroidRouterFactory(Vertx vertx, DBConnector dbConnector)
    {
        router = Router.router(vertx);
        mDBConnector = dbConnector;
    }

    /**
     * get router object
     * @return router
     */
    public Router getRouter() {
        return router;
    }

    private void pageRoute() {

        JadeTemplateEngine jadeTemplateEngine = JadeTemplateEngine.create();

        // main page
        router.route().path("/").handler(new PageHandler() {
            @Override
            public void handling() {
                if (isLogin()) {
                    redirectTo("/projectmain");
                    return;
                }

                rendering(jadeTemplateEngine, WebdroidConstant.Path.HTML + "/welcome");
            }
        });

        // main page
        router.route().path("/projectmain").handler(new PageHandler() {
            @Override
            public void handling() {
                if (!isLogin()) {
                    redirectTo("/");
                    return;
                }
                context.put("name", session.get("name")); //jade에서 projectmain 에서 쓰임
                JsonArray params = JsonUtil.createJsonArray((Integer) session.get("id"));

                mDBConnector.query(Query.MY_PROJECT, params, new SQLResultHandler<ResultSet>(this) {
                    @Override
                    public void success(ResultSet resultSet) {
                        List<JsonObject> resultList = resultSet.getRows();
                        List<JsonObject> filteredList = new ArrayList<>(resultList);

                        context.put("projects", resultList);

                        filteredList.removeIf(obj -> obj.getInteger("isImportant", 0) == 0);
                        context.put("favorates", filteredList);
                        rendering(jadeTemplateEngine, WebdroidConstant.Path.HTML + "/projectmain");  //jade변환한 파일이름
                    }
                });
            }
        });

        // setting page
        router.route().path("/setting").handler(new PageHandler() {
            @Override
            public void handling() {

                context.put("name", session.get("name")); //jade에서 projectmain 에서 쓰임
                JsonArray params = JsonUtil.createJsonArray((Integer) session.get("id"));
                rendering(jadeTemplateEngine, WebdroidConstant.Path.HTML + "/setting");
            }
        });
    }

    /**
     * template resource routing also handling client req
     */
    public void requestHandling() {
        // sign in
        String[] signinParams = {"user_id", "user_pw"};
        router.post("/signin").handler(new RequestHandler(false, signinParams) {
            @Override
            public void reqRecvParams(Map<String, Object> params) {
                if (isLogin())
                    session.destroy();
                logger.debug(req.params().toString());
                JsonArray dbParams = JsonUtil.createJsonArray(params.get(signinParams[0]), params.get(signinParams[1]));

                if ("true".equals(req.getParam("save_id"))) {
                    context.addCookie(Cookie.cookie("saveID", req.getParam("save_id")));
                    context.addCookie(Cookie.cookie("id", params.get("user_id").toString()));
                }

                mDBConnector.query(Query.SIGN_IN, dbParams, new SQLResultHandler<ResultSet>(this) {
                    @Override
                    public void success(ResultSet resultSet) {

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
                    }
                });
            }
        });

        // sign up
        String[] signupParams = {"user_id", "user_name", "user_pw"};
        router.post("/signup").handler(new RequestHandler(false, signupParams) {
            @Override
            public void reqRecvParams(Map<String, Object> params) {

                JsonArray dbParams = JsonUtil.createJsonArray(
                        params.get(signupParams[0]),
                        params.get(signupParams[1]),
                        params.get(signupParams[2]));

                mDBConnector.update(Query.SIGN_UP, dbParams, new SQLResultHandler<UpdateResult>(this) {
                    @Override
                    public void success(UpdateResult result) {
                        if (result.getUpdated() > 0) {
                            sendJsonResult(200, true, ResultMessage.PW_CHECKED);
                        } else
                            sendJsonResult(200, false, ResultMessage.PW_FAIL);
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
                        params.get("project_desc"), session.get("id"));
                mDBConnector.update(Query.NEW_PROJECT, dbParams,
                        new SQLResultHandler<UpdateResult>(this) {
                            @Override
                            public void success(UpdateResult resultSet) {
                                sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.SUCCESS);
                            }
                        });
            }
        });

        router.post("/gitsubmit").handler(new RequestHandler(true, "git_id") {
            @Override
            public void reqRecvParams(Map<String, Object> params) {
                System.out.println("gitsubmit");
                JsonArray dbParams = JsonUtil.createJsonArray(params.get("git_id"), session.get("id"));
                mDBConnector.update(Query.NEW_GIT, dbParams,
                        new SQLResultHandler<UpdateResult>(this) {
                            @Override
                            public void success(UpdateResult resultSet) {
                                sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.SUCCESS);
                            }
                        });
            }
        });

        router.post("/old_pwsubmit").handler(new RequestHandler(true, "passwd") {
            @Override
            public void reqRecvParams(Map<String, Object> params) {
                JsonArray dbParams = JsonUtil.createJsonArray(session.get("id"), params.get("passwd"));

                mDBConnector.query(Query.PW_CHECK, dbParams, new SQLResultHandler<ResultSet>(this) {
                    @Override
                    public void success(ResultSet resultSet) {

                        if (resultSet.getNumRows() == 1) {
                            sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.PW_CHECKED);
                        } else {
                            sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.PW_FAIL);
                        }
                    }
                });
            }
        });

        router.post("/new_pwsubmit").handler(new RequestHandler(true) {
            @Override
            public void reqRecvParams(Map<String, Object> params) {
                JsonArray dbParams = JsonUtil.createJsonArray(session.get("id") );
                mDBConnector.update(Query.NEW_PW, dbParams,
                        new SQLResultHandler<UpdateResult>(this) {
                            @Override
                            public void success(UpdateResult result) {
                                if (result.getUpdated() > 0) {
                                    sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.PW_CHECKED);
                                } else
                                    sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.PW_FAIL);
                            }
                        });
            }
        });

        router.post("/unsubscribe").handler(new RequestHandler(true, "passwd") {
            @Override
            public void reqRecvParams(Map<String, Object> params) {
                JsonArray dbParams = JsonUtil.createJsonArray(session.get("id"), params.get("passwd"));

                mDBConnector.query(Query.PW_CHECK, dbParams, new SQLResultHandler<ResultSet>(this) {
                    @Override
                    public void success(ResultSet resultSet) {

                        if (resultSet.getNumRows() == 1) {
                            sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.SUCCESS);

                        } else {
                            sendJsonResult(HttpStatusCode.SUCCESS, false, ResultMessage.PW_FAIL);
                        }
                    }
                });
            }
        });

        router.post("/final_unsubscribe").handler(new RequestHandler(true) {
            @Override
            public void reqRecvParams(Map<String, Object> params) {
                JsonArray dbParams = JsonUtil.createJsonArray(session.get("id"));
                mDBConnector.update(Query.UNSUBSCRIBE, dbParams,
                        new SQLResultHandler<UpdateResult>(this) {
                            @Override
                            public void success(UpdateResult resultSet) {
                                sendJsonResult(HttpStatusCode.SUCCESS, true, ResultMessage.SUCCESS);
                            }
                        });
            }
        });


    }


    /**
     * static resource routing
     */
    public void initStaticResource() {
        final String BOOTSTRAP = "/bootstrap/(css|fonts|js)/\\S+.\\S+";
        final String CSS = "/css/\\S+.(css)";
        final String JS = "/js/\\S+.(js)";
        final String IMG = "/images/\\S+.(jpeg|png|jpg|ico)";

        StaticHandler staticHandler = StaticHandler.create(WebdroidConstant.Path.STATIC);

        router.route().pathRegex(BOOTSTRAP).handler(staticHandler);
        router.route().pathRegex(JS).handler(staticHandler);
        router.route().pathRegex(CSS).handler(staticHandler);
        router.route().pathRegex(IMG).handler(staticHandler);


    }

    public void initBasicRouterhandler(Vertx vertx) {

        /**
         * req logging
         */
        router.route().handler(LoggerHandler.create());

        /**
         * when error
         */
        router.route().failureHandler(ErrorHandler.create(true));


        /**
         * for post req handling
         */
        router.route().handler(BodyHandler.create());


        router.route().handler(CookieHandler.create());

        SessionStore ss = null;
        if(IS_CLUSTERRED) {
            ss = ClusteredSessionStore.create(vertx, WebdroidConstant.ID.SESSION_MAP_NAME);
        } else {
            ss = LocalSessionStore.create(vertx, WebdroidConstant.ID.SESSION_MAP_NAME);
        }

        router.route().handler(SessionHandler.create(ss));
    }
}
