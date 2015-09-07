package org.webdroid.server.url.request;

import io.vertx.core.Vertx;
import org.webdroid.server.handler.RequestHandler;

import java.util.Map;

/**
 * Created by Seho on 2015-09-07.
 */
public class RunProjectRequestHandler extends RequestHandler {
    public static final String URL = "/run_vm";
    Vertx vertx = null;
    public RunProjectRequestHandler(Vertx vertx) { super(null, false, "command");
        this.vertx = vertx;
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        String msg = req.getParam("command");    // "run_vm"

        vertx.eventBus().send("socket", msg + "#ahn@mail.com");
    }

}
