package com.tingyu.venus.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tingyu.venus.model.table.UserTable;


public class UserInfoDB extends SQLiteOpenHelper {
    public UserInfoDB(@Nullable Context context) {
        super(context, "account.db", null, 1);
    }

    /**
     * 数据库创建时调用
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //执行创建用户表的SQL
        db.execSQL(UserTable.CREATE_TAB);

    }

    /**
     * 数据库更新时调用
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
