package com.tingyu.venus.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.tingyu.venus.model.db.DBHelper;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.model.table.ContactTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactDao {

    private DBHelper dbHelper; //保证联系人与邀请在同一个对象中操作

    public ContactDao(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<UserInfo> getContactList() {
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);
        List<UserInfo> userInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            UserInfo user = new UserInfo();
            user.setId(cursor.getString(cursor.getColumnIndex(ContactTable.COL_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USERNAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHONE)));
            user.setAvatar(cursor.getString(cursor.getColumnIndex(ContactTable.COL_AVATAR)));
            userInfoList.add(user);
        }

        //关闭资源
        cursor.close();
        //返回结果
        return userInfoList;

    }

    /**
     * 根据手机号查询联系人
     *
     * @param phone
     * @return
     */
    public UserInfo getContactByPhone(String phone) {
        if (phone == null || TextUtils.isEmpty(phone)) {
            return null;
        }
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //执行sql
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_PHONE + "=?";

        Cursor cursor = db.rawQuery(sql, new String[]{phone});
        UserInfo user = null;
        if (cursor.moveToNext()) {
            user = new UserInfo();
            user.setId(cursor.getString(cursor.getColumnIndex(ContactTable.COL_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USERNAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHONE)));
            user.setAvatar(cursor.getString(cursor.getColumnIndex(ContactTable.COL_AVATAR)));
        }
        //关闭资源
        cursor.close();
        return user;
    }

    /**
     * 根据手机号批量获得联系人信息
     * @param phones
     * @return
     */
    public List<UserInfo> getContactsByPhone(List<String> phones){
        if(phones==null || phones.size()<=0){
            return null;
        }

        List<UserInfo> userInfoList=new ArrayList<>();
        Iterator<String> iterator = phones.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            UserInfo userInfo = getContactByPhone(next);
            userInfoList.add(userInfo);
        }

        return userInfoList;

    }
    //保存单个联系人

    public void saveContact(UserInfo user,boolean isContact){
        if(user ==null){
            return;
        }
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //执行保存语句

        ContentValues values=new ContentValues();
        values.put(ContactTable.COL_ID,user.getId());
        values.put(ContactTable.COL_USERNAME,user.getUsername());
        values.put(ContactTable.COL_PHONE,user.getPhone());
        values.put(ContactTable.COL_AVATAR,user.getAvatar());
        values.put(ContactTable.COL_IS_CONTACT,isContact);
        db.replace(ContactTable.TAB_NAME,null,values);
    }

    //批量保存联系人

    public void saveContacts(List<UserInfo> contacts,boolean isContact){

        if(contacts ==null || contacts.size()<=0){
            return ;
        }
        for (UserInfo contact:contacts){
            saveContact(contact,isContact);
        }
    }

    //删除联系人信息

    public void deleteContactByPhone(String phone){
        if(phone==null || TextUtils.isEmpty(phone)){
            return;
        }
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //执行删除sql

        db.delete(ContactTable.TAB_NAME,ContactTable.COL_PHONE+"=?",new String[]{phone});

    }

}
