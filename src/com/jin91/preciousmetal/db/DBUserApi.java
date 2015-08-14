package com.jin91.preciousmetal.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.common.Config;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.common.database.bean.DBString;
import com.jin91.preciousmetal.common.database.dao.IDao;
import com.jin91.preciousmetal.common.database.db.DBManager;
import com.jin91.preciousmetal.common.util.CommonUtil;
import com.jin91.preciousmetal.ui.Constants;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lijinhua on 2015/6/4.
 */
public class DBUserApi {

    /**
     * 保存用户信息
     * 阻塞方法
     */
    public static void saveUserToCache(User user) {
        try {
            DBManager manager = DBManager.getInstance();
            IDao<DBString> dao = manager.openDao(DBString.class);
            DBString dbString = new DBString();
            dbString.string_key = Constants.DB_STRING_KEY_USER;
            List<DBString> values = dao.query(dbString);

            Gson gson = new Gson();
            Type type = new TypeToken<User>() {
            }.getType();
            String json = gson.toJson(user, type);

            DBString value = new DBString();
            value.string_key = Constants.DB_STRING_KEY_USER;
            value.string_value = json;
            if (!CommonUtil.isEmpty(values)) {
                value._id = values.get(0)._id;
            }
            dao.saveOrUpdate(value);
        } catch (Exception e) {
            if (Config.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 阻塞方法
     * 获取User
     */
    public static User getUserFromCache() {
        try {
            DBManager manager = DBManager.getInstance();
            IDao<DBString> dao = manager.openDao(DBString.class);
            DBString dbString = new DBString();
            dbString.string_key = Constants.DB_STRING_KEY_USER;
            List<DBString> values = dao.query(dbString);
            if (!CommonUtil.isEmpty(values)) {
                DBString value = values.get(0);
                String json = value.string_value;
                Gson gson = new Gson();
                Type type = new TypeToken<User>() {
                }.getType();
                User user = gson.fromJson(json, type);
                return user;
            }
        } catch (Exception e) {
            if (Config.DEBUG) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除User
     * 阻塞方法
     *
     * @param string_key
     */
    public static void deleteTokenFromCache(String string_key) {
        try {
            DBManager manager = DBManager.getInstance();
            IDao<DBString> dao = manager.openDao(DBString.class);
            DBString dbString = new DBString();
            dbString.string_key = string_key;
            dao.delete(dbString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
