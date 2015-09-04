package org.webdroid.server.url.request;

import org.webdroid.db.DBConnector;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JqueryFileTree;

import java.util.Map;

/**
 * Created by micky on 2015. 9. 5..
 */
public class ProjectTreeRequestHandler extends RequestHandler {

    public static final String URL = "/make_filetree";

    public ProjectTreeRequestHandler() {
        super(null, false, "dir");
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        res.setChunked(true);
        res.write(JqueryFileTree.createHtmlRes(req.getParam("dir"))).end();
    }
}
