package com.jin91.preciousmetal.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.jin91.preciousmetal.common.api.entity.AccountReturn;
import com.jin91.preciousmetal.common.api.entity.Update;
import com.jin91.preciousmetal.common.api.entity.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.List;

/**
 * Created by Administrator on 2015/4/25.
 * json工具类
 */
public class JsonUtil {
    private static final Gson gson = new Gson();


    public static <T> T parse(String json, Class<T> classOfT) {
        try {
            JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(true);
            return gson.fromJson(reader, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parse(String json, TypeToken<T> type) {
        try {
            JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(true);
            return gson.fromJson(reader, type.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> parseList(String json, TypeToken<List<T>> type) {
        try {
            JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(true);
            return gson.fromJson(reader, type.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析会员中心返回的值,并把jsonp转换为json
     *
     * @param jsonp
     * @return
     */
    public static AccountReturn parseAccountReturn(String jsonp) {
        if (jsonp.indexOf("(") >= 0) {
            jsonp = jsonp.substring(jsonp.indexOf("(") + 1, jsonp.length() - 1);
        }
        return JsonUtil.parse(jsonp, AccountReturn.class);
    }

    public static UserInfo parseUserInfo(String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            String jsonStr = object.getString("Table");
            TypeToken<List<UserInfo>> type = new TypeToken<List<UserInfo>>() {
            };
            return JsonUtil.parseList(jsonStr, type).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Update parseUpdate(String json)
    {
        Update update = JsonUtil.parse(json, Update.class);
        if("1".equals(update.hn))
        {
            try {
                JSONObject object = new JSONObject(json);
                update.ifForce = object.getString("if");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return update;
    }
}
