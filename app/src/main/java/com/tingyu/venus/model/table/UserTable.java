package com.tingyu.venus.model.table;

/**
 * 创建用户表字段，创建表的SQL语句
 */
public class UserTable {

    public static final String TAB_NAME="tb_user";
    public static final String COL_ID="user_id";
    public static final String COL_USERNAME="username";
    public static final String COL_PHONE="phone";
    public static final String COL_AVATAR="avatar";


    public static final String CREATE_TAB="create table "+
                    TAB_NAME+" ("+
                    COL_ID+" text primary key,"+
                    COL_USERNAME+" text, "+
                    COL_PHONE+" text, "+
                    COL_AVATAR+" text);";




}
