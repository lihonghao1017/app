package com.jin91.preciousmetal.ui.news;

import android.app.Activity;

/**
 * Created by lijinhua on 2015/4/29.
 */
public interface NewsDetailPresenter {

    /**
     * 获取资讯的详情study
     * @param id
     */
    public void getNewsDetail(String tag,String id);

    /**分享
     * @param url
     * @param type_title 分享类型的title
     * @param content_title 内容的title
     * @param content
     */
    public void shareDialog(Activity activity,String url,String type_title,String content_title);
}
