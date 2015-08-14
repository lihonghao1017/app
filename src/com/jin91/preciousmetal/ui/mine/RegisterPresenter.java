package com.jin91.preciousmetal.ui.mine;

/**
 * Created by lijinhua on 2015/5/7.
 */
public interface RegisterPresenter {

    /**
     * 获取图片验证码的key
     * @param tag
     */
    public void getCaptchaKey(String tag);

    /**
     * 获取手机验证码
     * @param TAG
     * @param phone 手机号
     * @param key 验证码的key
     * @param imgCode  图片的验证码
     */
    public void regSendPhoneCode(String TAG,String phone,String key,String imgCode);

    /**
     * 检查手机是否存在
     * @param TAG
     * @param phone 手机号
     * @param key 验证码的key
     * @param imgCode  图片的验证码
     */
    public void checkPhoneExits(String TAG,String phone,String key,String imgCode);

    /**
     * 验证短信
     * @param TAG
     * @param smsCode 收到的短信数字
     * @param phone 手机号
     * @param u 调用发短信接口返回值
     */
    public void validateSms(String TAG,String smsCode,String phone,String u);

    /**
     * 找回密码，发送验证码，并设置密码
     * @param TAG
     * @param phone  手机号
     * @param imgCode 图片验证码
     * @param vid   取自验证码图片属性 rel值
     * @param uid 用户的uid 值为第一步提交后返回的值，需要第二步提交时需要提交。
     * @param pass  密码
     * @param rePass
     */
    public void fwdSetPwd(String TAG,String phone,String imgCode,String vid,String uid,String pass,String rePass);


    /**
     * 发送短信验证码
     * @param TAG
     * @param phone
     * @param imgCode
     * @param vid
     */
    public void fwdSendPhoneCode(String TAG,String phone,String imgCode,String vid);


    /**
     * 注册设置密码
     * @param tag
     * @param phone 手机号
     * @param smsCode  短信验证码
     * @param uid 发送验证码返回的uid
     * @param pass 密码
     * @param rePass 重复密码
     */
    public void regSetPwd(String tag,String phone,String smsCode,String uid,String pass,String rePass);
}
