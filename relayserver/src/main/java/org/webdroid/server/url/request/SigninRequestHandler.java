package org.webdroid.server.url.request;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.Cookie;
import org.webdroid.constant.HttpStatusCode;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JsonUtil;

import java.util.Map;

/**
 * signin request handler
 * Created by micky on 2015. 9. 5..
 */
public class SigninRequestHandler extends RequestHandler {

    public static final String URL = "/signin";

    static final String[] signinParams = {"user_id", "user_pw"};

    public SigninRequestHandler(DBConnector dbConnector) {
        super(dbConnector, false, signinParams);
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {
        if (isLogin())
            session.destroy();
        logger.debug(req.params().toString());
        JsonArray dbParams = JsonUtil.createJsonArray(params.get(signinParams[0]), params.get(signinParams[1]));

        if ("true".equals(req.getParam("save_id"))) {
            context.addCookie(Cookie.cookie("saveID", req.getParam("save_id")));
            context.addCookie(Cookie.cookie("id", params.get("user_id").toString()));
        }

        mDBConnector.query(Query.SIGN_IN, dbParams, new SQLResultHandler<ResultSet>(this) {
            @Override
            public void success(ResultSet resultSet) {

                if (resultSet.getNumRows() > 0) {
                    JsonObject userInfo = resultSet.getRows().get(0);
                    logger.debug(userInfo.toString());
                    session.put("id", userInfo.getInteger("u_id"));
                    session.put("name", userInfo.getString("name"));

                    //sendJsonResult(HttpStatusCode.SUCCESS, true,ResultMessage.SIGNED_IN);
                    redirectTo("/projectmain");

                } else {
                    sendJsonResult(HttpStatusCode.SUCCESS, false,
                            ResultMessage.CHECK_ID_PW);
                }
            }
        });
    }
}
