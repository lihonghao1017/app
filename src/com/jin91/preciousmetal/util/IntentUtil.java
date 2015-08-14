package com.jin91.preciousmetal.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by lijinhua on 2015/5/5.
 * 意图的工具类
 */
public class IntentUtil {

    /**
     * 启动浏览器
     *
     * @param mContext
     * @param tel
     */
    public static void startBrower(Context mContext, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        mContext.startActivity(intent);
    }

    /**
     * 拨打电话
     *
     * @param mContext
     * @param tel      只需要传入电话号码，不加tel
     */
    public static void callPhone(Context mContext, String tel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        Uri content_url = Uri.parse("tel:" + tel);
        intent.setData(content_url);
        mContext.startActivity(intent);
    }

    /**
     * 发送邮件
     *
     * @param mContext
     * @param email
     */
    public static void sendEmail(Context mContext, String email) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        Uri content_url = Uri.parse(email);
        intent.setData(content_url);
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        } else {
            MessageToast.showToast("邮件app不存在", 0);
        }
    }
}
