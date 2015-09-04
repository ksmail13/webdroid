package org.webdroid.server.handler;

import org.webdroid.db.DBConnector;

import java.util.HashMap;
import java.util.Map;

public abstract class RequestHandler extends RouteHandler {
    protected String[] requestParameters = null;
    protected boolean chkLogin = false;

    protected DBConnector mDBConnector = null;

    private RequestHandler(DBConnector dbConnector) {
        mDBConnector = dbConnector;
    }

    /**
     * Request handler not web page
     * @param checkLogin this request need auth
     * @param params parameter names.
     */
    public RequestHandler (DBConnector dbConnector, boolean checkLogin, String... params) {
        this(dbConnector);
        chkLogin = checkLogin;
        requestParameters = params;
    }

    /**
     * Reqeust handler not web page
     * @param checkLogin this request need auth
     */
    public RequestHandler (DBConnector dbConnector, boolean checkLogin) {
        this(dbConnector, checkLogin, null);
    }

    @Override
    public void handling() {
        if(chkLogin && !isLogin()) {
            sendJsonResult(401, false, "This operation will sign in");
            return ;
        }


        if(requestParameters == null) {
            handlingWithParams(null);
            return ;
        }

        HashMap<String, Object> params = new HashMap<>();
        // Check each parameters
        for (String requestParameter : requestParameters) {
            String receiveParam = req.getParam(requestParameter);
            if(receiveParam != null)
                params.put(requestParameter, receiveParam);
            else {
                reqRecvParamsLess(requestParameter);
                return ;
            }
        }
        handlingWithParams(params);
    }

    /**
     * This method will execute when parameter is not enough
     */
    public void reqRecvParamsLess(String paramKey) {
        sendJsonResult(200, false, "you should include parameter "+paramKey);
    }

    /**
     * request handling
     * @param params parameters from client
     */
    public abstract void handlingWithParams(Map<String, Object> params);
}
