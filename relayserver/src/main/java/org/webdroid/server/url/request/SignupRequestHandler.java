package org.webdroid.server.url.request;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.UpdateResult;
import org.webdroid.constant.Query;
import org.webdroid.constant.ResultMessage;
import org.webdroid.db.DBConnector;
import org.webdroid.db.SQLResultHandler;
import org.webdroid.server.handler.RequestHandler;
import org.webdroid.util.JsonUtil;

import java.util.Map;

/**
 * Created by micky on 2015. 9. 5..
 */
public class SignupRequestHandler extends RequestHandler{
    public static final String URL = "/signup";

    static final String[] signupParams = {"user_id", "user_pw", "user_name"};

    public SignupRequestHandler(DBConnector dbConnector) {
        super(dbConnector, false, signupParams);
    }

    @Override
    public void handlingWithParams(Map<String, Object> params) {

        JsonArray dbParams = JsonUtil.createJsonArray(
                params.get(signupParams[0]),
                params.get(signupParams[1]),
                params.get(signupParams[2]));

        mDBConnector.update(Query.SIGN_UP, dbParams, new SQLResultHandler<UpdateResult>(this) {
            @Override
            public void success(UpdateResult result) {
                if (result.getUpdated() > 0) {
                    sendJsonResult(200, true, ResultMessage.PW_CHECKED);
                    //redirectTo("/");
                } else
                    sendJsonResult(200, false, ResultMessage.PW_FAIL);
            }
        });
    }
}
