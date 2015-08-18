package com.jin91.preciousmetal.ui.service;

import android.app.Activity;

import com.android.volley.VolleyError;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.ServiceApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.Finance;
import com.jin91.preciousmetal.customview.ShareDailog;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.wxapi.ShareModel;
import com.jin91.preciousmetal.wxapi.ShareQQWeibo;
import com.jin91.preciousmetal.wxapi.ShareSinaActivity;
import com.jin91.preciousmetal.wxapi.WXEntryActivity;

/**
 * Created by lijinhua on 2015/5/4.
 */
public class FinanceCalenPreImpl implements FinanceCalenPre {

    FinanceCalenView view;
    boolean isLoadSuccess; // 是否加载成功
    ShareDailog dialog;
    ShareModel shareModel;
    public FinanceCalenPreImpl(FinanceCalenView view) {
        this.view = view;
    }

    @Override
    public void getFinCalList(String tag,String date) {
        if (!isLoadSuccess) {
            view.showLoading();
        }
        ServiceApi.getEconomicLists(tag,date, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                Finance finance = JsonUtil.parse(json, Finance.class);
                if (!isLoadSuccess) {
                    view.hideLoading();
                }
                view.setItems(finance.Finance);
                isLoadSuccess = true;
                view.hideProcessDialog();
            }

            @Override
            public void onException(VolleyError e) {
                if (!isLoadSuccess) {
                    view.showNetErrView();
                }
                view.hideProcessDialog();
            }
        });
    }
    @Override
    public void shareDialog(final Activity activity, final String url, final String type_title, final String content_title) {
        shareModel = new ShareModel();
        shareModel.url = url;
        shareModel.type_title = type_title;
        shareModel.content_title = content_title;
        if (dialog == null) {
            int[] nameList = {R.string.wechat_friend, R.string.friends, R.string.sina_weibo, R.string.QQ_weibo};
            int[] iconList = {R.drawable.share_wefriends_selector, R.drawable.share_fricircle_selector, R.drawable.share_sina_selector, R.drawable.share_qqweibo_selector};
            dialog = new ShareDailog(activity, iconList, nameList, new ShareDailog.OnClickLinstener() {
                @Override
                public void onClick(int position) {

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
}
