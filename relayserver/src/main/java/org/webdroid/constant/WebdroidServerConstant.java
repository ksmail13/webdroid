package org.webdroid.constant;

/**
 * Created by 민규 on 2015-08-07.
 */
public abstract class WebdroidServerConstant {
    public static class Path {

        /**
         * resource path
         */
        public final static String WEBROOT = "./webroot";
        /**
         * static resource path
         */
        public final static String STATIC = WEBROOT + "/static";

        /**
         * html file path
         */
        public final static String HTML = STATIC;

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
