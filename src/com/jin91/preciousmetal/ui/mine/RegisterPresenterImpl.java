package com.jin91.preciousmetal.ui.mine;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.jin91.preciousmetal.common.api.UserApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.AccountReturn;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.util.ValidateUtil;

/**
 * Created by lijinhua on 2015/5/7.
 */
public class RegisterPresenterImpl implements RegisterPresenter {

    private RegisterView view;

    public RegisterPresenterImpl(RegisterView view) {
        this.view = view;
    }

    @Override
    public void getCaptchaKey(String tag) {
        UserApi.getCaptchaKey(tag, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1).replace("\"", "");
                }
                view.showErrorMsg("");
                view.setCaptchaKey(json);
            }

            @Override
            public void onException(VolleyError e) {
                view.showNetError();
            }
        });
    }

    @Override
    public void regSendPhoneCode(String TAG, String phone, String key, String imgCode) {
        if (TextUtils.isEmpty(phone)) {
            view.showErrorMsg("手机号不能为空");
            return;
        }
        if (!ValidateUtil.isMobile(phone)) {
            view.showErrorMsg("手机号格式错误");
            return;
        }
        if (TextUtils.isEmpty(imgCode)) {
            view.showErrorMsg("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(key)) {
            view.showErrorMsg("请点击图片刷新验证码");
            return;
        }
        view.hideSoftInput();
        view.showLoading();
        view.showErrorMsg("");
        UserApi.regSendPhonCode(TAG, phone, key, imgCode, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1);
                }
                view.hidLoading();
                AccountReturn accountReturn = JsonUtil.parse(json, AccountReturn.class);
                if ("2".equals(accountReturn.status)) {
                    view.showErrorMsg("");
                    view.handleSuccess(accountReturn.u);
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
    public void checkPhoneExits(String TAG, String phone, String key, String imgCode) {
        if (TextUtils.isEmpty(phone)) {
            view.showErrorMsg("手机号不能为空");
            return;
        }
        if (!ValidateUtil.isMobile(phone)) {
            view.showErrorMsg("手机号格式错误");
            return;
        }
        if (TextUtils.isEmpty(imgCode)) {
            view.showErrorMsg("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(key)) {
            view.showErrorMsg("请点击图片刷新验证码");
            return;
        }
        view.hideSoftInput();
        view.showLoading();
        view.showErrorMsg("");
        UserApi.checkPhoneExits(TAG, phone, key, imgCode, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1);
                }
                view.hidLoading();
                AccountReturn accountReturn = JsonUtil.parse(json, AccountReturn.class);
                if ("1".equals(accountReturn.status)) {
                    view.showErrorMsg("");
                    view.checkPhoneSuccess();
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
    public void validateSms(String TAG, String smsCode, String phone, String u) {
        view.hideSoftInput();
        view.showLoading();
        UserApi.validateSms(TAG, smsCode, phone, u, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1);
                }
                view.hidLoading();
                AccountReturn accountReturn = JsonUtil.parse(json, AccountReturn.class);
                if ("1".equals(accountReturn.status)) {
                    view.smsValidateSuccess();
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
    public void fwdSetPwd(String TAG, String phone, String imgCode, String vid, String uid, String pass, String rePass) {
        if (TextUtils.isEmpty(pass)) {
            view.showErrorMsg("密码不能为空");
            return;
        } else if (!ValidateUtil.checkPassword(pass)) {
            view.showErrorMsg("密码必须是6-32位");
            return;
        } else if (TextUtils.isEmpty(rePass)) {
            view.showErrorMsg("重复密码不能为空");
            return;
        } else if (!ValidateUtil.checkPassword(rePass)) {
            view.showErrorMsg("重复密码必须是6-32位");
            return;
        } else if (!pass.equals(rePass)) {
            view.showErrorMsg("两次的密码不一致");
            return;
        }
        view.hideSoftInput();
        view.showLoading();
        view.showErrorMsg("");
        UserApi.fwdAndSetPwd(TAG, phone, imgCode, "2", vid, uid, pass, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1);
                }
                view.hidLoading();
                AccountReturn accountReturn = JsonUtil.parse(json, AccountReturn.class);
                if ("2".equals(accountReturn.status)) {
                    view.showErrorMsg("");
                    view.handleSuccess(accountReturn.u);
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
    public void fwdSendPhoneCode(String TAG, String phone, String imgCode, String vid) {
        if (TextUtils.isEmpty(phone)) {
            view.showErrorMsg("手机号不能为空");
            return;
        }
        if (!ValidateUtil.isMobile(phone)) {
            view.showErrorMsg("手机号格式错误");
            return;
        }
        if (TextUtils.isEmpty(imgCode)) {
            view.showErrorMsg("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(vid)) {
            view.showErrorMsg("请点击图片刷新验证码");
            return;
        }
        view.hideSoftInput();
        view.showLoading();
        view.showErrorMsg("");
        UserApi.fwdAndSetPwd(TAG, phone, imgCode, "1", vid, "", "", new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1);
                }
                view.hidLoading();
                AccountReturn accountReturn = JsonUtil.parse(json, AccountReturn.class);
                if ("1".equals(accountReturn.status)) {
                    view.showErrorMsg("");
                    view.handleSuccess(accountReturn.u);
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
    public void regSetPwd(String tag, String phone, String smsCode, String uid, String pass, String rePass) {
        if (TextUtils.isEmpty(pass)) {
            view.showErrorMsg("密码不能为空");
            return;
        } else if (!ValidateUtil.checkPassword(pass)) {
            view.showErrorMsg("密码必须是6-32位");
            return;
        } else if (TextUtils.isEmpty(rePass)) {
            view.showErrorMsg("重复密码不能为空");
            return;
        } else if (!ValidateUtil.checkPassword(rePass)) {
            view.showErrorMsg("重复密码必须是6-32位");
            return;
        } else if (!pass.equals(rePass)) {
            view.showErrorMsg("两次的密码不一致");
            return;
        }
        view.hideSoftInput();
        view.showLoading();
        view.showErrorMsg("");
        UserApi.regSetPwd(tag, phone, smsCode, uid, rePass, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                if (json.indexOf("(") >= 0) {
                    json = json.substring(json.indexOf("(") + 1, json.length() - 1);
                }
                view.hidLoading();
                AccountReturn accountReturn = JsonUtil.parse(json, AccountReturn.class);
                if ("111".equals(accountReturn.status)) {
                    view.showErrorMsg("");
                    view.handleSuccess("");
                } else if ("-41".equals(accountReturn.status)) {
                    view.showErrorMsg("手机号已经存在");
                } else if ("-42".equals(accountReturn.status)) {
                    view.showErrorMsg("邮箱地址已经注册");
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

}
