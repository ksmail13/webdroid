package org.webdroid.constant;

/**
 * Database queries
 * Created by 민규 on 2015-08-07.
 */
public class Query {
    public final static String SIGN_UP =
            "insert into user" +
            "(id, passwd, join_time, name)" +
            "value" +
            "(?,password(?),now(),?)";

    public final static String SIGN_IN =
            "select " +
            "u_id, " +
            "name " +
            "from user " +
            "where id=? and passwd=password(?)";

    public final static String NEW_PROJECT =
            "insert into project\n" +
            "(p_name, p_descript, p_creator, create_date)\n" +
            "value\n" +
            "(?, ?, ?, now());";

    public final static String MY_PROJECT =
            "select\n" +
            "p.p_id as id,\n" +
            "p.p_name as name,\n" +
            "p.p_descript as description,\n" +
            "p.p_path as path,\n" +
            "p.p_is_important as isImportant,\n" +
            "p.create_date as createDate,\n" +
            "up.u_id as userId\n" +
            "from project as p, USER_PROJECT as up\n" +
            "where p.p_id = up.p_id and up.u_id = ?;";

    public final static String  NEW_GIT=
            "update user\n" +
            "set git_id=? \n"+
            "where u_id=? ;";


    public final static String  PW_CHECK=
            "SELECT count(*) as cnt\n"+
            "FROM user \n"+
            "where u_id=? and passwd=password(?)";


    public final static String  NEW_PW=
            "UPDATE user\n"+
            "SET passwd=password(?) \n"+
            "where u_id=?;";

    public final static String  UNSUBSCRIBE=
            "update user\n" +
            "set is_enable='0' \n"+
            "where u_id=?;";

        public final static String USERALINFOPROFILE =

                "SELECT id, passwd, name, git_id, introduce\n" +
                        "FROM user\n" +
                        "WHERE u_id = ?\n";

        public final static String NEW_INTRODUCE =
                "update user " +
                        "set introduce = ? " +
                        "where u_id = ?";

}
