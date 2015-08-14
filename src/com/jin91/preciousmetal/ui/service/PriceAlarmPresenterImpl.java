package com.jin91.preciousmetal.ui.service;

import com.android.volley.VolleyError;
import com.jin91.preciousmetal.common.api.ServiceApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.PriceAlarm;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.util.JsonUtil;

/**
 * Created by lijinhua on 2015/5/21.
 */
public class PriceAlarmPresenterImpl implements PriceAlarmPresenter {

    private PriceAlarmView view;
    private String uid = "";

    public PriceAlarmPresenterImpl(PriceAlarmView view) {
        this.view = view;
        User user = PreciousMetalAplication.INSTANCE.user;
        if (user != null) {
            uid = user.ID;
        }
    }

    @Override
    public void getAlarmList(String tag, boolean isShowloading, final int alarmType) {
        if (isShowloading) {
            view.showLoading();
        }

        ServiceApi.alarmList(tag, uid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                PriceAlarm alarm = JsonUtil.parse(json, PriceAlarm.class);
                if (alarmType == 1) {
                    // 本地提醒
                    if (alarm != null && alarm.Local != null && alarm.Local.size() > 0) {
                        view.setAlarmList(alarm.Local);
                        view.hideLoading();
                    } else {
                        view.showNoDataView();
                    }
                } else {
                    // 短信提醒
                    if (alarm != null && alarm.Mobile != null && alarm.Mobile.size() > 0) {
                        view.setAlarmList(alarm.Mobile);
                        view.hideLoading();
                    } else {
                        view.showNoDataView();
                    }
                }

            }

            @Override
            public void onException(VolleyError e) {
                view.showNetErrView();
            }
        });
    }

    @Override
    public void deleteAlarm(String tag, final int position, String deleteK) {

        view.showProcessDialog();
        ServiceApi.deleteAlarm(tag, uid, deleteK, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                view.hideProcessDialog();
                if ("1".equals(json)) {
                    view.delAlarmSuc(position);
                } else {
                    view.showToastMessage("删除失败");
                }
            }

            @Override
            public void onException(VolleyError e) {
                view.hideProcessDialog();
                view.showToastMessage("网络错误");
            }
        });
    }


}
