package com.tingyu.venus.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tingyu.venus.model.table.ContactTable;
import com.tingyu.venus.model.table.GroupChatTable;
import com.tingyu.venus.model.table.InvitationTable;
import com.tingyu.venus.model.table.MessageTable;
import com.tingyu.venus.model.table.ChatRecordTable;

public class DBHelper extends SQLiteOpenHelper {

    /**
     *
     * @param context
     * @param name 表名，根据用户不同创建不同名称的表
     */
    public DBHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建联系人表
        db.execSQL(ContactTable.CREATE_TAB);
        //创建邀请表
        db.execSQL(InvitationTable.CREATE_TAB);
        //创建联系人消息表
        db.execSQL(MessageTable.CREATE_TAB);
        //创建联系人聊天记录表
        db.execSQL(ChatRecordTable.CREATE_TAB);
        //创建群组表
        db.execSQL(GroupChatTable.CREATE_TAB);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
