package com.jin91.preciousmetal.ui.service;

import com.jin91.preciousmetal.common.api.entity.FinanceCalen;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/4.
 */
public interface FinanceCalenView {

    public void setItems(List<FinanceCalen> list);

    /**
     * 显示加载对话框
     */
    public void showLoading();

    /**
     * 隐藏加载对话框
     */
    public void hideLoading();

    /**
     * 显示网络错误对话框
     */
    public void showNetErrView();

    public void hideProcessDialog();
}
