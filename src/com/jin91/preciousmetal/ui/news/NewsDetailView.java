package com.jin91.preciousmetal.ui.news;

import com.jin91.preciousmetal.common.api.entity.Study;

/**
 * Created by lijinhua on 2015/4/29.
 */
public interface NewsDetailView {

    public void setStudy(Study study);


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
}
