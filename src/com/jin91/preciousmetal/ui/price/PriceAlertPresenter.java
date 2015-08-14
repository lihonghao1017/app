package com.jin91.preciousmetal.ui.price;

import com.jin91.preciousmetal.common.api.entity.Alert;

/**
 * Created by lijinhua on 2015/5/20.
 */
public interface PriceAlertPresenter {

    /**
     * 保存提醒
     * @param tag
     * @param alert
     */
    public void saveAlert(String tag,Alert alert);

    /**
     * 获取手机号
     * @param tag
     */
    public  void  getMobile(String tag);
}
