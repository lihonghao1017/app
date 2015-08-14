package com.jin91.preciousmetal.ui.price;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.jin91.preciousmetal.common.api.PriceApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.Alert;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;

/**
 * Created by lijinhua on 2015/5/20.
 */
public class PriceAlertPresenterImpl implements PriceAlertPresenter {

    private PriceAlertView view;
    private String uid;

    public PriceAlertPresenterImpl(PriceAlertView view) {
        this.view = view;
        User user = PreciousMetalAplication.INSTANCE.user;
        if (user == null) {
            uid = "";
        } else {
            uid = user.ID;
        }

    }

    @Override
    public void saveAlert(String tag, Alert alert) {
        if (TextUtils.isEmpty(alert.price)) {
            view.showToastMessage("请输入价格");
            return;
        }
        if (TextUtils.isEmpty(alert.tradeCode)) {
            view.showToastMessage("请选择要提醒的行情");
            return;
        }
        view.hideInput();
        view.showProcessDialog();
        PriceApi.saveAlert(tag, uid, alert, new ResultCallback() {

            @Override
            public void onEntitySuccess(String json) {
                view.hideProcessDialog();
                if ("0".equals(json)) {
                    view.showToastMessage("添加数量已超过指定数值");
                    return;
                } else {
                    view.saveAlertSuccess();
                }

            }

            @Override
            public void onException(VolleyError e) {
                view.hideProcessDialog();
                view.showToastNet();
            }
        });

    }

    @Override
    public void getMobile(String tag) {
        view.hideInput();
        view.showProcessDialog();
        PriceApi.getMobile(tag, uid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {

                if ("-1".equals(json)) {
                    view.hideProcessDialog();
                    view.showToastMessage("手机号不存在，请在\"账号管理\"完善您的手机号！");
                    return;
                } else if ("-0".equals(json)) {
                    view.hideProcessDialog();
                    view.showToastMessage("手机号不存在");
                    return;
                }
                view.setPhone(json);

            }

            @Override
            public void onException(VolleyError e) {
                view.hideProcessDialog();
                view.showToastNet();
            }
        });
    }
}
