package com.tingyu.venus.model.table;

/**
 * 封装创建邀请表的字段
 */
public class InvitationTable {

    public static final String TAB_NAME="tab_invitation"; //创建表名


    public static final String COL_USER_ID="user_id";
    public static final String COL_USER_PHONE="user_phone";
    public static final String COL_USERNAME="username";

    public static final String COL_GROUP_ID="group_id";
    public static final String COL_GROUP_NAME="group_name";

    public static final String COL_REASON="reason";
    public static final String COL_STATUS="status";//状态


    public static final String CREATE_TAB="create table "
            +TAB_NAME+" ( "
            +COL_USER_ID+" text primary key,"
            +COL_USERNAME+" text,"
            +COL_USER_PHONE+" text,"
            +COL_GROUP_ID+" Long,"
            +COL_GROUP_NAME+" text,"
            +COL_REASON+" text,"
            +COL_STATUS+" Integer);";

}
