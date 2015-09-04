package org.webdroid.server.handler;

import io.vertx.ext.web.templ.JadeTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.db.DBConnector;

/**
 * page request handler
 * Created by 민규 on 2015-08-17.
 */
public abstract class PageHandler extends RouteHandler {

    protected TemplateEngine templateEngine = null;
    protected DBConnector mDBConnector = null;

    public PageHandler(DBConnector dbConnector) {
        templateEngine = JadeTemplateEngine.create();
        mDBConnector = dbConnector;
    }


    /**
     * rendering template with data
     * @param templEngine template engine (like jade)
     * @param path template file path
     */
    public void rendering(TemplateEngine templEngine, String path) {
        templEngine.render(context, path, renderRes -> {
            if(renderRes.succeeded()) {
                send(HttpStatusCode.SUCCESS, "text/html", renderRes.result().toString());
            } else {
                logger.error("render error ", renderRes.cause());
                send(HttpStatusCode.RUNTIME_ERROR, "text/html", renderRes.cause().getMessage());
            }
        });

    }
}
