package com.jin91.preciousmetal.ui.mine;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.jin91.preciousmetal.common.api.AccountMangerApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.AccountReturn;
import com.jin91.preciousmetal.common.api.entity.UserInfo;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.util.ValidateUtil;

/**
 * Created by lijinhua on 2015/5/8.
 */
public class AccountMangerPreImpl implements AccountMangerPre {

    AccountMangerView view;

    public AccountMangerPreImpl(AccountMangerView view) {
        this.view = view;
    }

    @Override
    public void getUserInfo(String tag, String uid) {
        view.showLoading();
        AccountMangerApi.getUserinfo(tag, uid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                UserInfo userInfo = JsonUtil.parseUserInfo(json);
                view.hidLoading();
                if (userInfo != null) {
                    view.setUserInfo(userInfo);
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
    public void setPwd(String tag, String pass, String new_repass, String guid, String smsCode) {

        if (TextUtils.isEmpty(smsCode)) {
            view.showErrorMsg("手机验证码不能为空");
            return;
        } else if (TextUtils.isEmpty(pass)) {
            view.showErrorMsg("新密码不能为空");
            return;
        }
//        else if (!ValidateUtil.checkPassword(pass)) {
//            view.showErrorMsg("新密码必须是6-32位");
//            return;
//        }
        else if (TextUtils.isEmpty(new_repass)) {
            view.showErrorMsg("确证密码不能为空");
            return;
        }
//        else if (!ValidateUtil.checkPassword(new_repass)) {
//            view.showErrorMsg("确证密码必须是6-32位");
//            return;
//        }
        else if (!pass.equals(new_repass)) {
            view.showErrorMsg("新密码和确认密码不一致");
            return;
        }
        view.showErrorMsg("");
        view.showLoading();
        view.hideSoftInput();
        AccountMangerApi.setPwd(tag, pass, guid, smsCode, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                AccountReturn accountReturn = JsonUtil.parseAccountReturn(json);
                view.hidLoading();
                if ("1".equals(accountReturn.status)) {
                    view.handSuccess();
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
    public void modifyPwd(String tag, String old_pass, String new_pass, String new_repass, String guid) {
        if (TextUtils.isEmpty(old_pass)) {
            view.showErrorMsg("旧密码不能为空");
            return;
        } else if (TextUtils.isEmpty(new_pass)) {
            view.showErrorMsg("新密码不能为空");
            return;
        }
//        else if (!ValidateUtil.checkPassword(new_pass)) {
//            view.showErrorMsg("新密码必须是6-32位");
//            return;
//        }
        else if (TextUtils.isEmpty(new_repass)) {
            view.showErrorMsg("确证密码不能为空");
            return;
        }
//        else if (!ValidateUtil.checkPassword(new_repass)) {
//            view.showErrorMsg("确证密码必须是6-32位");
//            return;
//        }
        else if (!new_pass.equals(new_repass)) {
            view.showErrorMsg("新密码和确认密码不一致");
            return;
        }
        view.showErrorMsg("");
        view.showLoading();
        view.hideSoftInput();
        AccountMangerApi.modifyPwd(tag, old_pass, new_pass, guid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                AccountReturn accountReturn = JsonUtil.parseAccountReturn(json);
                view.hidLoading();
                if ("1".equals(accountReturn.status)) {
                    view.handSuccess();
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
    public void getPassCode(String tag, String guid) {
        view.showLoading();
        view.hideSoftInput();
        AccountMangerApi.getPassCode(tag, guid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                AccountReturn accountReturn = JsonUtil.parseAccountReturn(json);
                view.hidLoading();
                if ("11".equals(accountReturn.status)) {
                    view.sendSmsSuccess();
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
    public void loginOut(String tag, String uid) {
        view.showLoading();
        AccountMangerApi.loginOut(tag, uid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                view.hidLoading();
                if ("1".equals(json.trim())) {
                    view.logoutSuccess();
                } else {
                    view.showErrorMsg("注销账户失败，请重试");
                }
            }

            @Override
            public void onException(VolleyError e) {
                view.hidLoading();
                view.showNetError();
            }
        });
    }


}
