package com.jin91.preciousmetal.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.util.MessageToast;
import com.tencent.weibo.sdk.android.api.WeiboAPI;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;

/**
 * Created by lijinhua on 2015/4/30.
 * 分享到qq微博
 */
public class ShareQQWeibo {


    public static final String TAG = "ShareQQWeiboActivity";
    Activity mContext;
    ShareModel shareModel;
    private WeiboAPI weiboAPI;//微博相关API
    private HttpCallback mCallBack;//回调函数


    public ShareQQWeibo(Activity mContext, ShareModel shareModel) {
        this.mContext = mContext;
        this.shareModel = shareModel;
        init();
    }


    public void init() {
        String accessToken = Util.getSharePersistent(mContext, "ACCESS_TOKEN");
        if (accessToken == null || "".equals(accessToken)) {
            long appid = Long.valueOf(Util.getConfig().getProperty("APP_KEY"));
            String app_secket = Util.getConfig().getProperty("APP_KEY_SEC");
            auth(appid, app_secket);
        } else {
            shareQQweibo(accessToken);
        }
    }


    private void auth(long appid, String app_secket) {
        final Context context = mContext.getApplicationContext();
        //注册当前应用的appid和appkeysec，并指定一个OnAuthListener
        //OnAuthListener在授权过程中实施监听
        AuthHelper.register(mContext, appid, app_secket, new OnAuthListener() {

            //如果当前设备没有安装腾讯微博客户端，走这里
            @Override
            public void onWeiBoNotInstalled() {
                Logger.i(TAG, "微博没有安装");
//                MessageToast.showToast("微博没有安装", 0);
                AuthHelper.unregister(mContext);
                Intent i = new Intent(mContext, Authorize.class);
                mContext.startActivity(i);
            }

            //如果当前设备没安装指定版本的微博客户端，走这里
            @Override
            public void onWeiboVersionMisMatch() {
                Logger.i(TAG, "微博版本不匹配");
//                MessageToast.showToast("微博版本不匹配", 0);
                AuthHelper.unregister(mContext);
                Intent i = new Intent(mContext, Authorize.class);
                mContext.startActivity(i);
            }

            //如果授权失败，走这里
            @Override
            public void onAuthFail(int result, String err) {
                Logger.i(TAG, "微博授权失败" + result);
//                MessageToast.showToast("授权失败", 0);
                AuthHelper.unregister(mContext);
            }

            //授权成功，走这里
            //授权成功后，所有的授权信息是存放在WeiboToken对象里面的，可以根据具体的使用场景，将授权信息存放到自己期望的位置，
            //在这里，存放到了applicationcontext中
            @Override
            public void onAuthPassed(String name, WeiboToken token) {
//                MessageToast.showToast("授权成功", 0);
                Logger.i(TAG, "授权成功");
                //

                Util.saveSharePersistent(context, "ACCESS_TOKEN", token.accessToken);
                Util.saveSharePersistent(context, "EXPIRES_IN", String.valueOf(token.expiresIn));
                Util.saveSharePersistent(context, "OPEN_ID", token.openID);
                Util.saveSharePersistent(context, "REFRESH_TOKEN", "");
                Util.saveSharePersistent(context, "CLIENT_ID", Util.getConfig().getProperty("APP_KEY"));
                Util.saveSharePersistent(context, "AUTHORIZETIME",
                        String.valueOf(System.currentTimeMillis() / 1000l));
                AuthHelper.unregister(mContext);
            }
        });

        AuthHelper.auth(mContext, "");
    }

    public void shareQQweibo(String accessToken) {
        mCallBack = new HttpCallback() {

            @Override
            public void onResult(Object object) {
                Logger.i(TAG, "mCallBack---" + object);
                ModelResult result = (ModelResult) object;
                if (result != null && result.isSuccess()) {
                    MessageToast.showToast(R.string.share_success, 0);
                } else {
                    MessageToast.showToast(R.string.share_fail, 0);
                }
            }
        };
        AccountModel account = new AccountModel(accessToken);
        weiboAPI = new WeiboAPI(account);
        String content = "[ " + shareModel.type_title + " ] " + shareModel.content_title;
        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.share_icon);
        weiboAPI.addPic(mContext, content, "json", 0, 0, bm, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);
    }
}
