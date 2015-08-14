package com.jin91.preciousmetal.ui.mine;

/**
 * Created by lijinhua on 2015/5/7.
 */
public interface RegisterView {

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
     * 图片验证码的key
     * @param key
     */
    public void setCaptchaKey(String key);

    /**
     * 处理成功
     * @param uid
     */
    public void handleSuccess(String uid);

    /**
     * 短信验证成功
     */
    public void smsValidateSuccess();

    /**
     * 检查手机是否存在，不存在返回时调用
     */
    public void checkPhoneSuccess();
}
