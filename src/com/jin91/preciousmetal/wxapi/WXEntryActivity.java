package com.jin91.preciousmetal.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.util.ImageUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import static com.tencent.mm.sdk.modelbase.BaseResp.ErrCode.ERR_AUTH_DENIED;
import static com.tencent.mm.sdk.modelbase.BaseResp.ErrCode.ERR_OK;
import static com.tencent.mm.sdk.modelbase.BaseResp.ErrCode.ERR_USER_CANCEL;

/**
 * Created by lijinhua on 2015/4/29.
 * 分享微信
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    public static final String TAG = "WXEntryActivity";
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private Context mContext;
    private static ShareModel shareModels;

    public static void actionLaunch(Context context, ShareModel shareModel) {
    	shareModels=shareModel;
    	Intent intent = new Intent(context, WXEntryActivity.class);
//        intent.putExtra("shareModel", shareModel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
//        shareModel = (ShareModel) getIntent().getSerializableExtra("shareModel");
        Logger.i(TAG, "onCreate----" + shareModels);
        if(shareModels==null)
        {
            finish();
        }

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(mContext, Constants.WECHAT_APP_ID, false);
        api.registerApp(Constants.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
        if (!api.isWXAppInstalled()) {
            MessageToast.showToast(R.string.weixin_not_install, 0);
            finish();
            return;
        }
        shareWeiXing();
        shareModels=null;
    }

    /**
     * 分享到微信
     */
    public void shareWeiXing() {
        if (shareModels == null) {
            finish();
            return;
        }
        WXWebpageObject localWXWebpageObject = new WXWebpageObject();
        localWXWebpageObject.webpageUrl = shareModels.url;
        WXMediaMessage localWXMediaMessage = new WXMediaMessage(localWXWebpageObject);
        localWXMediaMessage.title = "[ " + shareModels.type_title + " ] " + shareModels.content_title;
        localWXMediaMessage.description = "[ " + shareModels.type_title + " ] " + shareModels.content_title;
        Bitmap thumBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_icon);
        localWXMediaMessage.thumbData = ImageUtil.bmpToByteArray(thumBitmap, true);
        Logger.i(TAG, "share bitmap size----" + localWXMediaMessage.thumbData.length);
        if (localWXMediaMessage.thumbData.length > 32768)  // 32*1024 = 32768
        {
            localWXMediaMessage.thumbData = ImageUtil.bitmapresize(thumBitmap, 32);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = System.currentTimeMillis() + "";
        req.message = localWXMediaMessage;
        req.scene = shareModels.weixin_type == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        Logger.i(TAG, "检查分享参数是否法===" + req.checkArgs());
        api.sendReq(req);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result = 0;

        switch (baseResp.errCode) {
            case ERR_OK:
                result = R.string.errcode_success;
                break;
            case ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }
        Logger.i(TAG,getString(result));
        MessageToast.showToast(result, 1);
        finish();
    }
}
