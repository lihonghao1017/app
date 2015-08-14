package com.jin91.preciousmetal.ui.news;

import com.jin91.preciousmetal.common.api.entity.Study;

import java.util.List;

/**
 * Created by lijinhua on 2015/4/28.
 */
public interface NewsView {

    /**
     * 设置数据
     * @param list
     */
    public void setItems(List<Study> list);

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

    public void refreshComplete();
}
