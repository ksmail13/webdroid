package org.webdroid.constant;

/**
 * Created by ¹Î±Ô on 2015-08-07.
 */
public class Query {
    public final static String SIGN_UP =
            "insert into user" +
            "(id, passwd, join_time, name)" +
            "value" +
            "(?,?,now(),?)";

    public final static String SIGN_IN =
            "select" +
            "u_id," +
            "name" +
            "from user" +
            "where id=? and passwd=?";
}
