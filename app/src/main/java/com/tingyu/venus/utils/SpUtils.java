package com.tingyu.venus.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tingyu.venus.model.Model;

/**
 * SharedPreference 工具类，用于存储少量数据
 */
public final class SpUtils {

    public static final String IS_LOGINED_BEFORE ="is_logined_before" ;
    public static final String IS_NEW_INVITE = "is_new_invite"; //联系人邀请
    public static final String IS_NEW_MSG ="is_new_msg" ; //是否有新消息
    private static SpUtils instance;
    public static final String CURRENT_USER_ID = "current_user_id";
    private static final String FILE_NAME = "venus";
    private static SharedPreferences sp;

    private SpUtils() {
    }


    public static SpUtils getInstance() {
        if (instance == null) {
            instance= new SpUtils();
        }

        if (sp == null) {

            sp = Model.getInstance().getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return instance;
    }

    /**
     * 保存数据
     *
     * @param key
     * @param value
     */
    public void save(String key, Object value) {

        if (value instanceof String) {
            sp.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            sp.edit().putInt(key, (Integer) value).commit();
        }

    }

    /**
     * 获取String类型数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 获取Boolean类型数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public Boolean getBoolean(String key, Boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /**
     * 获取int类型数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }


}
