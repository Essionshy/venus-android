package com.tingyu.venus.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tingyu.venus.model.db.UserInfoDB;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.model.table.UserTable;
import com.tingyu.venus.utils.SpUtils;

/**
 * 访问用户数据库
 */
public class UserDao {


    private final UserInfoDB mHelper;


    public UserDao(Context context) {
        //加载时创建数据库
        mHelper = new UserInfoDB(context);


    }

    public void addAccount(String account){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put(UserTable.COL_ID,account);
        db.replace(UserTable.TAB_NAME,UserTable.COL_ID,values);

    }


    public void save(UserInfo user){
        SQLiteDatabase db = mHelper.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put(UserTable.COL_ID,user.getId());
        values.put(UserTable.COL_USERNAME,user.getUsername());
        values.put(UserTable.COL_PHONE,user.getPhone());
        values.put(UserTable.COL_AVATAR,user.getAvatar());

        db.replace(UserTable.TAB_NAME,null,values);

    }

    public UserInfo getUserInfo(String userId){
        //获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String sql="select * from "+UserTable.TAB_NAME+" where "+UserTable.COL_ID+ " =? ";
        Cursor cursor = db.rawQuery(sql, new String[]{userId});
        UserInfo userInfo=null;
        if(cursor.moveToNext()){
             userInfo = new UserInfo();
            userInfo.setId(cursor.getString(cursor.getColumnIndex(UserTable.COL_ID))); //用户ID
            userInfo.setPhone(cursor.getString(cursor.getColumnIndex(UserTable.COL_PHONE))); //用户手机
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex(UserTable.COL_USERNAME)));//用户名
            userInfo.setAvatar(cursor.getString(cursor.getColumnIndex(UserTable.COL_AVATAR)));//用户头像
        }
        //释放资源，关闭数据库连接
        db.close();
        return userInfo;


    }

    public UserInfo getCurrentUser() {
        return getUserInfo(SpUtils.getInstance().getString(SpUtils.CURRENT_USER_ID,""));
    }
}
