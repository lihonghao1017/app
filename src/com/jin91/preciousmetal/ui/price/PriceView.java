package com.jin91.preciousmetal.ui.price;

import com.jin91.preciousmetal.common.api.entity.Price;

import java.util.List;
import java.util.Map;

/**
 * Created by lijinhua on 2015/4/24.
 */
public interface PriceView{


    /**
     * 设置数据
     * @param map
     */
    public void setItems(Map<String,List<Price>> map);


    /**
     * 刷新完成，不管是成功或者失败都调用些方法
     */
    public void refreshComplete();
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
