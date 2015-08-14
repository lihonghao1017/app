package com.jin91.preciousmetal.ui.mine;

/**
 * Created by lijinhua on 2015/5/6.
 */
public interface LoginView {


    /**
     * 显示加载的对话框
     */
    public void showLoading();

    /**
     * 提示网络错误
     */
    public void showNetError();

    /**
     * 隐藏加载的对话框
     */
    public void hidLoading();

    /**显示错误信息
     * @param errMsg
     */
    public void showErrorMsg(String errMsg);

    /**
     * 隐藏输入法
     */
    public void hideSoftInput();

    /**
     * 登录成功
     */
    public void loginSuccess();

    /**
     * 设置的uid
     * @param uid
     */
    public void setUid(String uid);
}
