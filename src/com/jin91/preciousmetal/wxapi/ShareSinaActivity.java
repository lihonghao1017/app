package com.jin91.preciousmetal.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.util.MessageToast;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by lijinhua on 2015/4/29.
 * 分享到新浪
 */
public class ShareSinaActivity extends Activity implements IWeiboHandler.Response {

    private IWeiboShareAPI mWeiboShareAPI = null;

    Activity activity;
    ShareModel shareModel;

    public static void actionLaunch(Context mContext, ShareModel shareModel) {
        Intent intent = new Intent(mContext, ShareSinaActivity.class);
        intent.putExtra("shareModel", shareModel);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        shareModel = (ShareModel) getIntent().getSerializableExtra("shareModel");
        if (shareModel == null) {
            finish();
        }
        initSina();
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
        shareSina();
        shareModel=null;
    }

    public void initSina() {
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, Constants.SINA_APP_KEY);
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();


    }

    public void shareSina() {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj();
        weiboMessage.imageObject = getImageObj();
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        AuthInfo authInfo = new AuthInfo(activity, Constants.SINA_APP_KEY, Constants.SINA_REDIRECT_URL, Constants.SINA_SCOPE);
        String token = "";
        mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                MessageToast.showToast(arg0.getMessage(), 100);
            }

            @Override
            public void onComplete(Bundle bundle) {
                MessageToast.showToast("Complete",100);
            }

            @Override
            public void onCancel() {
                MessageToast.showToast("Cancel",100);
            }
        });

    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "[ " + shareModel.type_title + " ] " + shareModel.content_title;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) activity.getResources().getDrawable(R.mipmap.share_icon);
        imageObject.setImageObject(bitmapDrawable.getBitmap());
        return imageObject;
    }

    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     *
     * @param baseResponse 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResponse) {
        int result = 0;
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                result = R.string.share_success;
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                result = R.string.share_cancel;
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                result = R.string.share_fail;
                break;
        }
        MessageToast.showToast(result, 1);
        finish();
    }
}
