package org.webdroid.constant;

/**
 * Created by 민규 on 2015-08-07.
 */
public abstract class WebdroidConstant {
    public static class Path {

        /**
         * resource path
         */
        public final static String WEBROOT = "./webroot";
        public final static String TEMPL_WEBROOT = "./target/classes/webroot/";

        /**
         * static resource path
         */
        public final static String STATIC = WEBROOT + "/static";
        public final static String TEMPL_STATIC = TEMPL_WEBROOT+"/static";

        /**
         * html file path
         */
        public final static String HTML = STATIC;
        public final static String TEMPL_HTML = TEMPL_STATIC;

        /**
         * image resource path
         */
        public final static String IMG_PATH = STATIC + "/images";
    }

    public static class ID {

        public static final String SESSION_MAP_NAME = "webdroid";
    }

    public static class Conf {
        public static final boolean IS_CLUSTERED = false;
        public static final String SERVER_ENCODING = "utf-8";

        public static final boolean DEBUG = true;
    }

    public static class Message {
        public static final String SIGNED_IN = "signed in";
        public static final String CHECK_ID_PW = "Check your ID & password";

        public static final String SIGNED_UP = "sign up";
        public static final String SINGED_UP_FAIL = "Fail sign up";
    }

    public static class StatusCode {
        public final static int SUCCESS = 200;
        public final static int NOT_FOUND = 404;
        public final static int RUNTIME_ERROR = 500;
    }
}
