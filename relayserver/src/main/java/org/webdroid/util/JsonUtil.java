package org.webdroid.util;

import io.vertx.core.json.JsonArray;

/**
 * Created by ¹Î±Ô on 2015-08-12.
 */
public class JsonUtil {
    public static JsonArray createJsonArray(Object... params) {
        JsonArray array = new JsonArray();
        for (Object param : params) {
            array.add(param);
        }

        return array;
    }
}
