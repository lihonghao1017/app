package com.jin91.preciousmetal.ui.service;

import android.app.Activity;

/**
 * Created by lijinhua on 2015/5/4.
 */
public interface FinanceCalenPre {

    /**
     * 得到财经日历的列表
     */
    public   void getFinCalList(String tag,String date);

    /**分享
     * @param url
     * @param type_title 分享类型的title
     * @param content_title 内容的title
     * @param content
     */
    public void shareDialog(Activity activity,String url,String type_title,String content_title);
}
