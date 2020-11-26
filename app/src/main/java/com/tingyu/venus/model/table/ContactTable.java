package com.tingyu.venus.model.table;

/**
 * 创建联系人表的信息
 */
public class ContactTable {

    public static final String TAB_NAME = "tab_contact";

    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PHONE = "phone";
    public static final String COL_AVATAR = "avatar";

    public static final String COL_IS_CONTACT = "is_contact";//是否是联系人

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_ID + " Long primary key,"
            + COL_AVATAR + " text,"
            + COL_PHONE + " text,"
            + COL_USERNAME + " text,"
            + COL_IS_CONTACT + " Integer);";

}
