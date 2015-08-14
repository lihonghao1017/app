package com.jin91.preciousmetal.ui.mine;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/11.
 */
public interface GuoXinMsgView<T> {

    /**
     * 设置数据
     * @param suggestNoet 建议的备注信息
     * @param list
     */
    public void setItems(String suggestNoet,List<T> list);

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

    /**
     * toast显示网络错误
     */
    public void showToastNetErr();

    /**
     * toast显示没有更多数据
     */
    public void showToastNoMoreData();

    /**
     * 显示没有数据的view
     */
    public void showNoDataView();
}
