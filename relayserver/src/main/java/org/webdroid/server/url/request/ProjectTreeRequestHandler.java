package org.webdroid.server.url.request;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JqueryFileTree;
import org.webdroid.util.JsonUtil;

import java.util.Map;

/**
 * Created by micky on 2015. 9. 5..
 */
public class ProjectTreeRequestHandler extends RequestHandler {

    public static final String URL = "/make_filetree";

    public ProjectTreeRequestHandler(DBConnector dbConnector) {super(dbConnector, false, "dir", "root");
    }
    @Override
    public void handlingWithParams(Map<String, Object> params) {

        res.setChunked(true);
        res.write(JqueryFileTree.createHtmlRes(req.getParam("dir"),req.getParam("root"))).end();
        }
    }
