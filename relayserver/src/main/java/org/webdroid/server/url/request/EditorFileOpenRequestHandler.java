package org.webdroid.server.url.request;

//import io.vertx.core.json.JsonObject;
//import org.webdroid.constant.HttpStatusCode;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JqueryFileTree;

import java.util.Map;

/**
 * Created by Seho on 2015-09-06.
 */
public class EditorFileOpenRequestHandler extends RequestHandler {

    public static final String URL = "/openfile";

    public EditorFileOpenRequestHandler() {
        super(null, false, "filepath");
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        res.setChunked(true);
        res.write(JqueryFileTree.openFileFromTree(req.getParam("filepath"))).end();

        //JsonObject obj = new JsonObject();
        //obj.put("innerText", JqueryFileTree.openFileFromTree(req.getParam("filepath")));
        //obj.put("ext",JqueryFileTree.fileExt(req.getParam("filepath")));
        //send(HttpStatusCode.SUCCESS, "application/json", obj.toString());
    }
}
