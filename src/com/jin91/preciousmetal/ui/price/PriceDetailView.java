package com.jin91.preciousmetal.ui.price;

import com.jin91.preciousmetal.common.api.entity.Price;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/19.
 */
public interface PriceDetailView {

    public void setPrice(Price price);

    /**
     * 显示dialog
     */
    public void showLoading();

    /**
     * 隐藏dialog
     */
    public void hideLoading();

    /**
     * 显示toast网络错误
     */
    public void showToastNetErro();

    public void showToast(String message);

    public void setPriceKList(List<Price> mList);

    public void setPriceTimeList(List<Price> timeList);
    /**
     * 行情K线请求网络返回，不管是成功还是失败都调用这个方法，
     */
    public void priceKNetReturn();

    /**
     * 分时图请求网络返回,也是不管成功还是失败都调用这个方法
     */
    public void priceTimeNetReturn();
}
