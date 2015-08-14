package com.jin91.preciousmetal.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;

/**
 * Created by lijinhua on 2015/5/6.
 */
public class UserHelper {

    private static final String TAG = "UserHelper";
    private static final String USER_JSON = "user_json";

    private static final String SHAREDPREFERENCE_NAME = "sharedpreferences_preciouse";
    private static UserHelper mInstance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    /**
     * context传application
     *
     * @return
     */
    public synchronized static UserHelper getInstance() {
        if (mInstance == null) {
            mInstance = new UserHelper();
        }

        return mInstance;
    }

    private UserHelper() {
        mSharedPreferences = PreciousMetalAplication.getContext().getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }


    /**
     * 保存用户信息到SharePrefence
     *并设置application
     */
    public void SavaUserToShare(String  jsonUser) {
        PreciousMetalAplication.getINSTANCE().user = JsonUtil.parse(jsonUser,User.class);
        Logger.i(TAG,"saveUserToshare==="+PreciousMetalAplication.getINSTANCE().user);
        putString(USER_JSON, jsonUser);
    }

    /**
     * 从SharePrefence恢复用户信息
     */
    public User fromShareUserInfo() {
        Logger.i(TAG, "从SharePrefence恢复用户信息");
        String userGson = getString(USER_JSON, "");
        User user = null;
        if (!TextUtils.isEmpty(userGson)) {
            user = new Gson().fromJson(userGson, User.class);
        }
        return user;
    }

    /**
     * 判断用户是否登录
     */
    public boolean isLogin() {
        String userGson = getString(USER_JSON, "");
        if (!TextUtils.isEmpty(userGson)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存update的json数据在service里面使用
     *
     * @param json
     */
    public void setUpdateJson(String json) {
        putString("update", json);
    }

    public String getUpdateJson() {
        return getString("update", "");
    }

    public void setVersion(String version) {
        putString("version", version);
    }

    public String getVersion() {
        return getString("version", "");
    }

    /**
     * 设置即时建议的消息数量
     *
     * @param number
     * @return
     */
    public void setIdeaMsgNumber(int number) {
        putInt("idea", number);
    }

    public int getIdeaMsgNumber() {
        return getInt("idea", 0);
    }

    /**
     * 设置国鑫消息
     *
     * @param number
     */
    public void setGuoXinCount(int number) {
        putInt("guoXinCount", number);
    }

    public int getGuoXinCount() {
        return getInt("guoXinCount", 0);
    }

    /**
     * 设置顾问回复
     *
     * @param number
     */
    public void setReplyCount(int number) {
        putInt("replyCount", number);
    }

    public int getReplyCount() {
        return getInt("replyCount", 0);
    }

    /**
     * 设置资讯的消息
     *
     * @param number
     */
    public void setNewsCount(int number) {
        putInt("newsCount", number);
    }

    public int getNewsCount() {
        return getInt("newsCount", 0);
    }


    /**
     * 清除用户信息
     */
    public void clearUserJson() {
        putString(USER_JSON, "");
    }

    /**
     * ----------------------------------sharedPreferences的api---------------------------------*
     */
    public void putString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void putInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void putLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void putFloat(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }


    public String getString(String key, String value) {
        return mSharedPreferences.getString(key, value);
    }

    public int getInt(String key, int value) {
        return mSharedPreferences.getInt(key, value);
    }

    public long getLong(String key, long value) {
        return mSharedPreferences.getLong(key, value);
    }

    public float getFloat(String key, float value) {
        return mSharedPreferences.getFloat(key, value);
    }

    public boolean getBoolean(String key, boolean value) {
        return mSharedPreferences.getBoolean(key, value);
    }


}
