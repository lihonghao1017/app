package com.jin91.preciousmetal.ui.price;

/**
 * Created by lijinhua on 2015/5/20.
 */
public interface PriceAlertView {

    public void showProcessDialog();

    public void hideProcessDialog();

    public void showToastNet();

    public void showToastMessage(String message);

    public void saveAlertSuccess();

    /**
     * 设置手机号
     * @param phone
     */
    public void setPhone(String phone);

    /**
     * 隐藏输入法
     */
    public void hideInput();
}
