package org.webdroid.server;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.webdroid.constant.ServerConfigure;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.server.url.page.*;
import org.webdroid.server.url.request.*;

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
        router.vertx = vertx;
        router.initBasicRouterhandler(vertx);
        router.initStaticResource();
        router.pageRoute();
        router.requestHandling();

        return router.getRouter();
    }
    private Vertx vertx = null;
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

        // index page
        router.route().path(WelcomePageHandler.URL).handler(new WelcomePageHandler());

        // user main page
        router.route().path(UserIndexPageHandler.URL).handler(new UserIndexPageHandler(mDBConnector));

        // user profile page
        router.route().path(UserProfilePageHandler.URL).handler(new UserProfilePageHandler(mDBConnector));

        // project list page
        router.route().path(UserProjectListPageHandler.URL).handler(new UserProjectListPageHandler(mDBConnector));

        // setting page
        router.route().path(UserSettingPageHandler.URL).handler(new UserSettingPageHandler());

        // signup page
        router.route().path(SignupPageHandler.URL).handler(new SignupPageHandler());

        // signin page
        router.route().path(SigninPageHandler.URL).handler(new SigninPageHandler(vertx));

        // password change password page
        router.route().path(PwChangePageHandler.URL).handler(new PwChangePageHandler());
    }

    /**
     * template resource routing also handling client req
     */
    public void requestHandling() {
        // sign in
        router.post(SigninRequestHandler.URL).handler(new SigninRequestHandler(mDBConnector));

        // sign up
        router.post(SignupRequestHandler.URL).handler(new SignupRequestHandler(mDBConnector));

        // sign out handler
        router.route(SignoutRequestHandler.URL).handler(new SignoutRequestHandler());

        // create project handler
        router.post(CreateProjectRequestHandler.URL).handler(new CreateProjectRequestHandler(mDBConnector));

        // gitid handler
        router.post(GitIdUpdateRequestHandler.URL).handler(new GitIdUpdateRequestHandler(mDBConnector));

        // introduce handler
        router.post(IntroduceUpdateRequestHandler.URL).handler(new IntroduceUpdateRequestHandler(mDBConnector));

        // user image upload handler
        router.post(UserImageUploadReqeustHandler.URL).handler(new UserImageUploadReqeustHandler(mDBConnector, vertx));

        // check password handler
        router.post(CheckPasswordRequestHandler.URL).handler(new CheckPasswordRequestHandler(mDBConnector));

        router.post(ChangePasswordRequestHandler.URL).handler(new ChangePasswordRequestHandler(mDBConnector));

        // unsubscribe handler
        //router.post("/unsubscribe").handler(new CheckPasswordRequestHandler(mDBConnector));
        router.post(UnsubscribeRequestHandler.URL).handler(new UnsubscribeRequestHandler(mDBConnector));

        // get project files handler
        router.route(ProjectTreeRequestHandler.URL).handler(new ProjectTreeRequestHandler());

        router.route(EditorFileOpenRequestHandler.URL).handler(new EditorFileOpenRequestHandler());

        router.route(RunProjectRequestHandler.URL).handler(new RunProjectRequestHandler(vertx));

        router.post(PasswordInitRequestHandler.URL).handler(new PasswordInitRequestHandler(mDBConnector));

        router.post(IdCheckRequestHandler.URL).handler(new IdCheckRequestHandler(mDBConnector));

        router.post(ProjectmainRequestHandler.URL).handler(new ProjectmainRequestHandler(mDBConnector));

        router.post(ProjectUploadRequestHandler.URL).handler(new ProjectUploadRequestHandler(mDBConnector));



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

    /**
     * set log, error, body and cookie handler on router
     * @param vertx
     */
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
