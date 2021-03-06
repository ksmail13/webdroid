package org.webdroid.constant;

/**
 * Created by 민규 on 2015-08-07.
 */
public abstract class WebdroidConstant {
    public static class Path {

        public final static String CONFIGURE = "./configure";

        public final static String UPLOAD_ROOT = "./user-upload";
        public final static String UPLOAD_IMG = UPLOAD_ROOT + "/images";
        public final static String STATIC_UPLOAD_ROOT = "../../../user-upload";
        public final static String STATIC_UPLOAD_IMG = STATIC_UPLOAD_ROOT+"/images";
        public final static String UPLOAD_PROJECT = UPLOAD_ROOT + "/projects";

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

}
