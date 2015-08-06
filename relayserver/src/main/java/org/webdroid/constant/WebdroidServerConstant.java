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
    }
}
