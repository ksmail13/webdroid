package org.webdroid.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

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

    /**
     * simple json createJsonResult
     * @param result req createJsonResult
     * @param message requst result message
     * @return
     */
    public static JsonObject createJsonResult(boolean result, String message) {
        return new JsonObject().put("result", result).put("message", message);
    }
}
