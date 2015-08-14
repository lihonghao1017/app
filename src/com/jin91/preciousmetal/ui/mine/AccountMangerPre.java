package com.jin91.preciousmetal.ui.mine;

/**
 * Created by lijinhua on 2015/5/8.
 */
public interface AccountMangerPre {

    /**
     * 获取用户的信息
     * @param tag
     * @param uid
     */
    public void getUserInfo(String tag,String uid);


    /**
     * 设置密码
     * @param tag
     * @param pass
     * @param new_repass
     * @param guid
     * @param smsCode
     */
    public void setPwd(String tag,String pass,String new_repass,String guid,String smsCode);

    /**
     * 修改密码
     * @param tag
     * @param old_pass
     * @param new_pass
     * @param new_repass  重复新密码
     * @param guid
     */
    public void modifyPwd(String tag,String old_pass,String new_pass,String new_repass,String guid);


    /**
     * 用于设置密码时获取手机验证码
     * @param tag
     * @param guid
     */
    public void getPassCode(String tag,String guid);


    /**
     * 注销登录
     * @param tag
     * @param uid
     */
    public void loginOut(String tag,String uid);

}
