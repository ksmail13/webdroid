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
<<<<<<< HEAD
            "from project as p, USER_PROJECT as up\n" +
            "where p.p_is_working != '0' and p.p_id = up.p_id and up.u_id = ?;";
=======
    "from project as p, USER_PROJECT as up\n" +
            "where p.p_id = up.p_id and up.u_id = ?;";
>>>>>>> origin/master

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

    public final static String USER_ALL_INFOPROFILE =

            "SELECT id, name, git_id, introduce, user_img\n" +
            "FROM user\n" +
            "WHERE u_id = ?\n";

    public final static String NEW_INTRODUCE =
            "update user " +
            "set introduce = ? " +
            "where u_id = ?";

    public final static String PROFILE_IMG_UPLOAD =
            "update user " +
            "set user_img = ? " +
            "where u_id = ?";


    public final static String SET_RANDOM_PW=
            "UPDATE user\n"+
            "SET passwd=password(FLOOR('10000' + rand() * '90000')) \n"+
            "where id=?;";

    public final static String ID_CHECK=
            "SELECT count(*) as cnt\n"+
            "FROM user \n"+
            "WHERE id = ?";

    public final static String GET_PROFILE_IMAGE = "SELECT \n" +
            "user_img as path\n" +
            "from user\n" +
            "where u_id='?';";


    public final static String DELETE_PROJECT =
            "update project "+
                    "set p_is_working = '0', p_is_important = '0' "+
                    "where p_id = ?";

    public final static String FAVORATE_PROJECT =
            "update project "+
                    "set p_is_important = '1' "+
                    "where p_id = ?";

    public final static String CANCEL_FAVORATE_PROJECT =
            "update project "+
                    "set p_is_important = '0' "+
                    "where p_id = ?";

    public final static String GET_PATH=
            "SELECT p_path\n"+
            "FROM project\n"+
            "WHERE p_id=?";

}
