package com.jin91.preciousmetal.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.common.api.entity.ValidRoom;
import com.jin91.preciousmetal.common.database.api.DBApi;
import com.jin91.preciousmetal.common.database.api.IDBCallback;
import com.jin91.preciousmetal.common.database.api.IDBManagerCallback;
import com.jin91.preciousmetal.common.database.bean.DBString;
import com.jin91.preciousmetal.common.database.dao.IDao;
import com.jin91.preciousmetal.common.database.db.DBManager;
import com.jin91.preciousmetal.common.util.CommonUtil;
import com.jin91.preciousmetal.ui.Constants;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lijinhua on 2015/5/6.
 */
public class DBStringApi {

    public static void saveRoomListAsync(final String uid, final List<ValidRoom> mList, final IDBCallback<DBString> callback) {
        DBApi.loadDBManager(new IDBManagerCallback() {
            @Override
            public void onException(Exception e) {
                callback.exception(e);
            }

            @Override
            public void onDBManagerCallback(DBManager manager) {
                IDao<DBString> dao = manager.openDao(DBString.class);
                DBString dbString = new DBString();
                dbString._uid = uid;
                dbString.string_key = Constants.DB_STRING_KEY_ROOM;

                List<DBString> values = dao.query(dbString);

                Gson gson = new Gson();
                Type type = new TypeToken<List<ValidRoom>>() {
                }.getType();
                String json = gson.toJson(mList, type);

                DBString value = new DBString();
                value._uid = uid;
                value.string_key = Constants.DB_STRING_KEY_ROOM;
                value.string_value = json;
                if (!CommonUtil.isEmpty(values)) {
                    value._id = values.get(0)._id;
                }
                dao.saveOrUpdate(value);
                callback.success(value);
            }
        });
    }

    public static void loadRoomListAsync(final String uid, final IDBCallback<List<ValidRoom>> callback)
    {
        DBApi.loadDBManager(new IDBManagerCallback() {
            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onDBManagerCallback(DBManager manager) {
                List<ValidRoom> hotlist = null;
                IDao<DBString> dao = manager.openDao(DBString.class);
                DBString dbString = new DBString();
                dbString._uid = uid;
                dbString.string_key = Constants.DB_STRING_KEY_ROOM;
                List<DBString> values = dao.query(dbString);
                if (!CommonUtil.isEmpty(values)) {
                    DBString value = values.get(0);
                    String json = value.string_value;
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ValidRoom>>() {
                    }.getType();
                    hotlist = gson.fromJson(json, type);
                }
                callback.success(hotlist);
            }
        });
    }
}
