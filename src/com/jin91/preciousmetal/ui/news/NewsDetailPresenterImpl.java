package com.jin91.preciousmetal.ui.news;

import android.app.Activity;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.NewsApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.Study;
import com.jin91.preciousmetal.customview.ShareDailog;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.wxapi.ShareModel;
import com.jin91.preciousmetal.wxapi.ShareQQWeibo;
import com.jin91.preciousmetal.wxapi.ShareSinaActivity;
import com.jin91.preciousmetal.wxapi.WXEntryActivity;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by lijinhua on 2015/4/29.
 */
public class NewsDetailPresenterImpl implements NewsDetailPresenter {

    NewsDetailView view;
    ShareDailog dialog;

    public NewsDetailPresenterImpl(NewsDetailView view) {
        this.view = view;
    }

    @Override
    public void getNewsDetail(String tag, String id) {
        view.showLoading();
        NewsApi.getNewsDetail(tag, id, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                Study study = parseStudyDetail(json);
                if (study != null) {
                    view.hideLoading();
                    view.setStudy(study);
                } else {
                    view.showNetErrView();
                }

            }

            @Override
            public void onException(VolleyError e) {
                view.showNetErrView();
            }
        });
    }

    @Override
    public void shareDialog(final Activity activity, final String url, final String type_title, final String content_title) {
        if (dialog == null) {


            int[] nameList = {R.string.wechat_friend, R.string.friends, R.string.sina_weibo, R.string.QQ_weibo};
            int[] iconList = {R.drawable.share_wefriends_selector, R.drawable.share_fricircle_selector, R.drawable.share_sina_selector, R.drawable.share_qqweibo_selector};
            dialog = new ShareDailog(activity, iconList, nameList, new ShareDailog.OnClickLinstener() {
                @Override
                public void onClick(int position) {
                    ShareModel shareModel = new ShareModel();
                    shareModel.url = url;
                    shareModel.type_title = type_title;
                    shareModel.content_title = content_title;
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

    public static Study parseStudyDetail(String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            String jsonStr = object.getString("table");

            TypeToken<List<Study>> type = new TypeToken<List<Study>>() {
            };
            return JsonUtil.parseList(jsonStr, type).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
