package com.jin91.preciousmetal.ui.mine;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.common.api.UserApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.AccountReturn;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.common.api.entity.ValidRoom;
import com.jin91.preciousmetal.common.database.api.IDBCallback;
import com.jin91.preciousmetal.common.database.bean.DBString;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.db.DBStringApi;
import com.jin91.preciousmetal.db.DBUserApi;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.jin91.preciousmetal.util.UserHelper;
import com.jin91.preciousmetal.util.ValidateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/6.
 */
public class LoginPresenterImpl implements LoginPresenter {

    LoginView view;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
    }

    @Override
    public void accountLogin(final String tag,String imie, final String username, final String password) {
        if (TextUtils.isEmpty(username)) {
            view.showErrorMsg("账户不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            view.showErrorMsg("密码不能为空");
            return;
        }
        view.showErrorMsg("");
        view.hideSoftInput();
        view.showLoading();
        UserApi.accountLogin(tag,imie, username, password, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {

                // 这里面是jsonp需要转换为json
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1);
                }
                Logger.i(tag, "convertJson---" + json);
                AccountReturn loginReturn = JsonUtil.parse(json, AccountReturn.class);
                if ("1".equals(loginReturn.status)) {
                    Logger.i(tag, "登录成功");
                    accGuidGetUid(tag, loginReturn.guid, username,  loginReturn.password);
                } else {
                    view.hidLoading();
                    view.showErrorMsg(loginReturn.info);
                }
            }

            @Override
            public void onException(VolleyError e) {
                view.hidLoading();
                view.showNetError();
            }
        });
    }

    @Override
    public void getPhoneCode(String tag, String phone) {
        if (TextUtils.isEmpty(phone)) {
            view.showErrorMsg("手机号不能为空");
            return;
        } else if (!ValidateUtil.isMobile(phone)) {
            view.showErrorMsg("手机号格式错误");
            return;
        }
        view.showErrorMsg("");
        view.hideSoftInput();
        view.showLoading();
        UserApi.getPhoneCode(tag, phone, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1);
                }
                view.hidLoading();
                AccountReturn accountReturn = JsonUtil.parse(json, AccountReturn.class);
                if ("1".equals(accountReturn.status)) {
                    view.showErrorMsg("");
                    view.setUid(accountReturn.u);
                } else {
                    view.showErrorMsg(accountReturn.info);
                }
            }

            @Override
            public void onException(VolleyError e) {
                view.hidLoading();
                view.showNetError();
            }
        });

    }

    @Override
    public void phoneLogin(final String tag,String imie, final String phone, String smsCode, String uid) {
        if (TextUtils.isEmpty(phone)) {
            view.showErrorMsg("手机号不能为空");
            return;
        } else if (!ValidateUtil.isMobile(phone)) {
            view.showErrorMsg("手机号格式错误");
            return;
        } else if (TextUtils.isEmpty(smsCode)) {
            view.showErrorMsg("验证码不能为空");
            return;
        }
        view.showErrorMsg("");
        view.hideSoftInput();
        view.showLoading();
        UserApi.phoneLogin(tag,imie, phone, smsCode, uid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                // 这里面是jsonp需要转换为json
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1);
                }
                Logger.i(tag, "convertJson---" + json);
                AccountReturn loginReturn = JsonUtil.parse(json, AccountReturn.class);
                if ("1".equals(loginReturn.status)) {
                    Logger.i(tag, "登录成功");
                    accGuidGetUid(tag, loginReturn.guid, phone, loginReturn.password);
                } else {
                    view.hidLoading();
                    view.showErrorMsg(loginReturn.info);
                }
            }

            @Override
            public void onException(VolleyError e) {
                view.hidLoading();
                view.showNetError();
            }
        });
    }

    /**
     * 根据guid获取uid
     *
     * @param tag
     * @param guid
     * @param username
     * @param pwd
     */
    private void accGuidGetUid(final String tag, final String guid, final String username, final String pwd) {
        Logger.i(tag, "线程获取uid");
        UserApi.accGuidGetUid(tag, guid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {

                User user = JsonUtil.parse(json, User.class);
                // 这里面没有返回密码用户名，再把这些设置进去,这里面设置密码主要是为了区分是修改密码还是设置密码
                // 如果为null或者"" 设置密码,如果不为null,则修改密码
//                if(user==null)
//                {
//                    return;
//                }
                user.loginName = username;
                user.Upwd = pwd;
//                user.setType(getLoginType(username)); // 这里面先不保存
                if (!TextUtils.isEmpty(user.ID)) {
                    //保存用户信息到sharepre
//                    PreciousMetalAplication.INSTANCE.user = user;
                    UserHelper.getInstance().SavaUserToShare(new Gson().toJson(user));
                    DBUserApi.saveUserToCache(user); // 同步保存到数据库
                    getVailRoom(tag, user.ID);
                } else {
                    // 没有获取到uid就一直获取
                    accGuidGetUid(tag, guid, username, pwd);
                }
            }

            @Override
            public void onException(VolleyError e) {
                view.hidLoading();
                view.showNetError();
            }
        });
    }

    /**
     * 获取用户的播间
     *
     * @param tag
     * @param uid
     */
    @Override
    public void getVailRoom(String tag, final String uid) {
        UserApi.getVailRoom(tag, uid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    String jsonStr = object.getString("Table");
                    TypeToken<List<ValidRoom>> type = new TypeToken<List<ValidRoom>>() {
                    };
                    List<ValidRoom> validRoomList = JsonUtil.parseList(jsonStr, type);
                    for (ValidRoom validRoom : validRoomList) {
                        validRoom.UserId = uid;
                    }
                    saveRoomToDbSyn(uid, validRoomList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onException(VolleyError e) {
                view.hidLoading();
                view.showNetError();
            }
        });
    }

    /**
     * 异步的保存用户播间room到数据库
     */
    public void saveRoomToDbSyn(String uid, List<ValidRoom> validRoomList) {
        DBStringApi.saveRoomListAsync(uid, validRoomList, new IDBCallback.SimpleDBCallback<DBString>() {
            @Override
            public void onSuccess(DBString callback) {
                view.loginSuccess();
                view.hidLoading();

            }

            @Override
            public void onException(Exception e) {
                super.onException(e);
                view.hidLoading();
                MessageToast.showToast("异步的保存用户播间room到数据库失败",0);
            }
        });
    }

}
