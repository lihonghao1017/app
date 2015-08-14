package com.jin91.preciousmetal.ui.service;

/**
 * Created by lijinhua on 2015/5/21.
 */
public interface PriceAlarmPresenter {

    /**
     * 获取本地提醒列表
     *
     * @param tag
     * @param alarmType 1表示本地提醒，2表示短信提醒
     */
    public void getAlarmList(String tag,boolean isShowloading,int alarmType);

    public void deleteAlarm(String tag,int position,String deleteK);
}
