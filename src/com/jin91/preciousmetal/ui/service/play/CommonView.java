package com.jin91.preciousmetal.ui.service.play;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/15.
 */
public interface CommonView<T> {

    /**
     * 设置直播里面的数据
     * @param list
     */
    public void setDataList(List<T> list);
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
     * @param  noDataStr 显示没有数据的提示文字
     */
    public void showNoDataView(String noDataStr);


}
