package com.jin91.preciousmetal.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.UserApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.AllNotify;
import com.jin91.preciousmetal.common.api.entity.Update;
import com.jin91.preciousmetal.customview.CustomDialog;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.customview.ShareDailog;
import com.jin91.preciousmetal.services.DownLoadService;
import com.jin91.preciousmetal.ui.MainActivity;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.jin91.preciousmetal.util.UserHelper;
import com.jin91.preciousmetal.wxapi.ShareModel;
import com.jin91.preciousmetal.wxapi.ShareQQWeibo;
import com.jin91.preciousmetal.wxapi.ShareSinaActivity;
import com.jin91.preciousmetal.wxapi.WXEntryActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/5.
 * 设置
 */
public class SettingActivity extends BaseActivity {


    @ViewInject(R.id.iv_set_newMsg)
    ImageView iv_set_newMsg;
    LoadingDialog loadingDialog;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;
    ShareDailog dialog;

    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_setting);
        ViewUtils.inject(this);
        initialize();
    }

    @OnClick({R.id.tv_title_back, R.id.rl_shared_jin,R.id.rl_update_version,R.id.rl_msg_notify, R.id.rl_clear_cache, R.id.rl_feed_back, R.id.rl_use_protocol, R.id.rl_about_jin})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.rl_update_version: // 版本更新
                if (ClientUtil.serviceIsStart(DownLoadService.class.getName(), mContext)) {
                    MessageToast.showToast("正在下载更新...", 0);
                    return;
                }
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(mContext, false);
                }
                loadingDialog.show();
                checkUpdate();
                break;
            case R.id.rl_clear_cache: // 清除缓存
                Glide.with(mContext).onLowMemory();
                MessageToast.showToast("清除成功", 0);
                break;
            case R.id.rl_feed_back: // 意见反馈
                if (PreciousMetalAplication.getINSTANCE().user == null) {
                    LoginActivity.actionLaunch(mContext);
                    return;
                }
                FeedBackActivity.actionLaunch(mContext);
                break;
            case R.id.rl_use_protocol: // 用户协议
                UserProtocolActivity.actionLaunch(mContext);
                break;
            case R.id.rl_about_jin: // 关于国鑫
                AboutJinActivity.actionLaunch(mContext);
                break;
            case R.id.rl_msg_notify: // 消息提醒
            	MsgNotifyActivity.actionLaunch(mContext);
                break;
            case R.id.rl_shared_jin:
            	shareDialog(this);
            	break;
        }
    }
    public void shareDialog(final Activity activity) {
        if (dialog == null) {


            int[] nameList = {R.string.wechat_friend, R.string.friends, R.string.sina_weibo, R.string.QQ_weibo};
            int[] iconList = {R.drawable.share_wefriends_selector, R.drawable.share_fricircle_selector, R.drawable.share_sina_selector, R.drawable.share_qqweibo_selector};
            dialog = new ShareDailog(activity, iconList, nameList, new ShareDailog.OnClickLinstener() {
                @Override
                public void onClick(int position) {
                    ShareModel shareModel = new ShareModel();
                    shareModel.url = "http://m.91jin.com/zhuanti/hj3gfx/";
                    shareModel.type_title = "国鑫贵金属";
                    shareModel.content_title = "值得信赖的贵金属投资顾问";
                    dialog.dismiss();
                    switch (position) {

                        case 0: // 微信好友
                            shareModel.weixin_type = 0;
                            WXEntryActivity.actionLaunch(activity, shareModel);
                            break;
                        case 1: // 朋友圈
                            shareModel.weixin_type = 1;
                            WXEntryActivity.actionLaunch(activity, shareModel);
                            break;
                        case 2: // 新浪微博
//                            new ShareSina(activity, shareModel).shareSina();
                            ShareSinaActivity.actionLaunch(activity, shareModel);
                            break;
                        case 3:// 腾讯微博
                            new ShareQQWeibo(activity, shareModel);
                            break;
                    }
                }
            });
        }
        dialog.show();
    }

    @Override
    public String getRequestTag() {
        return "SettingActivity";
    }

    @Override
    public void initialize() {
        tv_title_title.setText(getString(R.string.setting));
        UserHelper userHelper = UserHelper.getInstance();
        if (!TextUtils.isEmpty(userHelper.getVersion()) && (!userHelper.getVersion().equals(ClientUtil.getVersionName(mContext)))) {
            iv_set_newMsg.setVisibility(View.VISIBLE);
        } else {
            iv_set_newMsg.setVisibility(View.INVISIBLE);
        }
    }

    private void checkUpdate() {
        String versionName = ClientUtil.getVersionName(mContext);
        if (TextUtils.isEmpty(versionName)) {
            return;
        }
        UserApi.updateVersion("SettingActivity", versionName, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                loadingDialog.dismiss();
                Update update = JsonUtil.parseUpdate(json);
                UserHelper userHelper = UserHelper.getInstance();
                if ("1".equals(update.hn)) {
                    //  有更新下载
                    userHelper.setUpdateJson(json);
                    CustomDialog customDialog;
                    if ("1".equals(update.ifForce)) {
                        // 强制升级
                        customDialog = new CustomDialog(mContext, new CustomDialog.CustomDialogListener() {
                            @Override
                            public void customButtonListener(int postion) {
                                if (postion == 2) {
                                    startDownload();
                                }
                            }
                        }, false, false);
                        customDialog.setHideTvCancel();
                    } else {
                        // 不是强制升级
                        customDialog = new CustomDialog(mContext, new CustomDialog.CustomDialogListener() {
                            @Override
                            public void customButtonListener(int postion) {
                                if (postion == 2) {
                                    // 启动downloadservice下载
                                    startDownload();
                                }
                            }
                        }, false, true);
                    }
                    customDialog.setTvDialogTitle(getString(R.string.dialog_title));
                    customDialog.setTvDialogContent(getString(R.string.update));
                    customDialog.setTvOkText(getString(R.string.dialog_ok));
                    customDialog.show();
                    iv_set_newMsg.setVisibility(View.VISIBLE);
                    // 更新首页的MainFragment里面的设置
                    Intent intent = new Intent(MainActivity.REFRESH_MESSAGE);
                    AllNotify notify = new AllNotify();
                    notify.t = 11;
                    intent.putExtra("allNotify",notify);
                    sendBroadcast(intent);
                } else {
                    // 没有更新
                    userHelper.setVersion(ClientUtil.getVersionName(mContext));
                    iv_set_newMsg.setVisibility(View.INVISIBLE);
                    MessageToast.showToast("已是最新版",0);
                }
            }

            @Override
            public void onException(VolleyError e) {
                loadingDialog.dismiss();
                MessageToast.showToast(R.string.net_error, 0);
            }
        });
    }

    private void startDownload() {
        DownLoadService.actionLaunch(mContext);
    }

}
