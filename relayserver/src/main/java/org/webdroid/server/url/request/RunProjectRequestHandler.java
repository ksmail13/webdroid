package org.webdroid.server.url.request;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.vm.PythonProcessController;

import java.util.Map;

/**
 * Created by Seho on 2015-09-07.
 */
public class RunProjectRequestHandler extends RequestHandler {
    public static final String URL = "/run_vm";
    Vertx vertx = null;
    PythonProcessController pythonProcessController;

    public RunProjectRequestHandler(Vertx vertx) { super(null, false, "command");
        this.vertx = vertx;
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        String msg = req.getParam("command");    // "run_vm@userId"
        pythonProcessController = new PythonProcessController();
        pythonProcessController.startProcess();
        if(msg.contains("run_vm")){
            pythonProcessController.inputCommand("run_vm 1");
        }
        /*
        vertx.eventBus().send("socket", msg + "#ahn@abc.abc");

        vertx.eventBus().consumer("frameBuffer",this::frameBufferSender);
        */

    }

    private void frameBufferSender(Message<Object> objectMessage) {
        JsonObject obj = new JsonObject();
        obj.put("frameBuffer", objectMessage.body().toString());
        send(HttpStatusCode.SUCCESS, "application/json", obj.toString());
    }

}
