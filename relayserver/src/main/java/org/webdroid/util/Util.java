package org.webdroid.util;

import java.util.Objects;

/**
 * Created by ¹Î±Ô on 2015-08-13.
 */
public class Util {

    /**
     * Check paremters valid
     * @param params
     * @return
     */
    public static boolean isValidParams(Object... params) {
        for(Object obj : params) {
            if(obj == null) return false;
        }
        return true;
    }

}
