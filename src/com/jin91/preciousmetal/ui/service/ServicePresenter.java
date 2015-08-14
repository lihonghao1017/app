package com.jin91.preciousmetal.ui.service;

/**
 * Created by lijinhua on 2015/5/12.
 */
public interface ServicePresenter {

    /**
     * 获取room的list
     * @param tag
     * @param uid 如果不存在传""
     */
    public void getRoomList(String tag,String uid);


    /**
     * 获取最新的直播动态
     * @param tag
     */
    public void getNewsPlayDy(String tag,String roomId);
    /**
     * 获取价格的公告(白银)
     * @param tag
     */
    public void getPriceNotice(String tag);

    /**
     * 发送评论或者提问或者回复
     * @param tag
     * @param type 2--回复或者交流
     *             1 提问
     * @param replyTo
     *             如果是交流传0,回复传回复的id,
     *             提问的时候不传
     * @param content
     */
    public void sendComment(String tag,String type,String replyTo,String content);
}
