package com.jin91.preciousmetal.ui.mine;


import com.google.gson.reflect.TypeToken;

/**
 * Created by lijinhua on 2015/5/11.
 */
public interface GuoXinMsgPresenter {

    /**获取
     * @param  isShowLoading 是否显示加载dialog
     * @param tag
     * @param type  1表示即时建议 2表示顾问回复    4--表示国鑫消息
     * @param startid
     * @param islater 是否加载更多
     */
    public  void getGuoXinMsgList(TypeToken typeToken, boolean isShowLoading, String tag, String type, String startid,String islater);
}
