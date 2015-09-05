package org.webdroid.server.url.request;

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
    }
}
