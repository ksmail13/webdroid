package org.webdroid.util;

import io.vertx.core.json.JsonObject;

/**
 * Created by ¹Î±Ô on 2015-08-07.
 */
public class RequestResult {
    /**
     * simple json result
     * @param result request result
     * @param message requst result message
     * @return
     */
    public static JsonObject result(boolean result, String message) {
        return new JsonObject().put("result", result).put("message", message);
    }
}
