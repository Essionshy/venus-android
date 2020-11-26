package com.tingyu.venus.model.table;

public final class GroupChatTable {

    public static final String TAB_NAME = "tbl_group";

    public static final String COL_GROUP_ID = "group_id";
    public static final String COL_GROUP_NAME = "group_name";
    public static final String COL_GROUP_AVATAR = "group_avatar";
    public static final String COL_GROUP_MEMBER = "group_member";
    public static final String COL_GROUP_CREATE_ID = "group_create_id";

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_GROUP_ID + " text primary key,"
            + COL_GROUP_NAME + " text,"
            + COL_GROUP_AVATAR + " text,"
            + COL_GROUP_CREATE_ID + " text,"
            + COL_GROUP_MEMBER + " text);";
}
