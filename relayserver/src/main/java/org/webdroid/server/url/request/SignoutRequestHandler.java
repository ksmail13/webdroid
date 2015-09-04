package org.webdroid.server.url.request;

import org.webdroid.db.DBConnector;
import org.webdroid.server.handler.RequestHandler;

import java.util.Map;

/**
 * Created by micky on 2015. 9. 5..
 */
public class SignoutRequestHandler extends RequestHandler {

    public static final String URL = "/signout";

    public SignoutRequestHandler() {
        super(null, false);
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        session.destroy();
        context.clearUser();
        redirectTo("/");
        //sendJsonResult(HttpStatusCode.FOUND, true, "sign out");
    }
}
