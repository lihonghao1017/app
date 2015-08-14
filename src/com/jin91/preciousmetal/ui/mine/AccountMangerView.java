package com.jin91.preciousmetal.ui.mine;

import com.jin91.preciousmetal.common.api.entity.UserInfo;

/**
 * Created by lijinhua on 2015/5/8.
 * 账号管理的view,包括获取用户信息和修改密码
 */
public abstract interface AccountMangerView {

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
     * 设置用户信息
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo);

    /**
     * 处理成功
     */
    public void handSuccess();

    /**
     * 发送验证码成功
     */
    public void sendSmsSuccess();

    /**
     * 注销成功
     */
    public void logoutSuccess();
}
