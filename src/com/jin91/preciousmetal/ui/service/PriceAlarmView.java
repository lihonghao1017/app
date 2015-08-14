package com.jin91.preciousmetal.ui.service;

import com.jin91.preciousmetal.common.api.entity.Alarm;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/21.
 */
public interface PriceAlarmView {


    public void setAlarmList(List<Alarm> list);

    /**
     * 显示加载对话框
     */
    public void showLoading();

    public void showProcessDialog();

    public void hideProcessDialog();

    /**
     * 删除提示成功
     */
    public void delAlarmSuc(int position);

    public void showToastMessage(String message);
    /**
     * 隐藏加载对话框
     */
    public void hideLoading();

    /**
     * 显示网络错误对话框
     */
    public void showNetErrView();

    /**
     * 显示没有数据的view
     */
    public void showNoDataView();
}
