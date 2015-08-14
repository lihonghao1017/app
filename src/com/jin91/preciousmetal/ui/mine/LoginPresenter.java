package com.jin91.preciousmetal.ui.mine;

/**
 * Created by lijinhua on 2015/5/6.
 */
public interface LoginPresenter {

    /**
     * 账号登录
     * @param tag
     * @param imie 手机的imie号
     * @param username
     * @param password
     */
    public void accountLogin(String tag,String imie,String username,String password);

    /**
     * 获取手机验证码
     * @param tag
     * @param phone
     */
    public void getPhoneCode(String tag,String phone);

    /**
     * 手机号登录
     * @param tag
     * @param phone
     * @param smsCode
     * @param uid
     */
    public void phoneLogin(String tag,String imie,String phone,String smsCode,String uid);

    public void getVailRoom(String tag, final String uid);
}
