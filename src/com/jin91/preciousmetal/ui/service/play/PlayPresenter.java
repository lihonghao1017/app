package com.jin91.preciousmetal.ui.service.play;

import com.google.gson.reflect.TypeToken;


/**
 * Created by lijinhua on 2015/5/13.
 * @param <TypeToken>
 */
public interface PlayPresenter<TypeToken> {

    /**
     * 获取直播第一页的列表
     * isShowLoading
     * @param  type 0,全部 1,白银 2，黄金 3,铜铝镍 4,是有
     * @param  startId 第一次传0,如果已经获取到，传最新的一条数据的id
     * */
    public void getDirectPlayFirstNewList(TypeToken typeToken, boolean isShowLoading,String type,String startId);
    /**
     * 获取直播第一页的列表
     * isShowLoading
     * @param  type 0,直播 1,交流 2，发言 3,主题 4,精华 5表示建议
     * @param  startId 第一次传0,如果已经获取到，传最新的一条数据的id
     * */
    public void getDirectPlayFirstList(TypeToken typeToken, boolean isShowLoading,String type,String startId);
    /**
     * 得到建议的列表页的第一页
     * @param isShowLoading
     * @param startId
     */
    public void getSuggestFirstList(boolean isShowLoading,String startId);

    public void getDirectPlayMoreNewList(TypeToken typeToken,String startId,String action,String type);
    
    /**
     * 获取直播更多的列表
     * isShowLoading
     * @param startId  获取第一页传0,获取下一页，传上一面最后一条数据的id
//     * @param action getlivedata 直播
                       getspeaksdata 交流
                       getmydatas

     */
    public void getDirectPlayMoreList(TypeToken typeToken,String startId,String action,String type);

    /**
     * 获取主题的更多
     * @param startId
     */
    public void getThemePlayMoreList(TypeToken typeToken,String startId,String action);

    /**
     * 获取播间里面的即时建议,这里面返回的json跟上面的格式不一样
     * isShowLoading
     * @param startId  获取第一页传0,获取下一页，传上一面最后一条数据的id
    //     * @param action getlivedata 直播
     */
    public void getSuggestMoreList(TypeToken typeToken,String startId,String action);

    /**
     * 获取公告的列表
     * @param tag
     * @param isShowLoading 是否显示loading
     * @param startid
     * @param islater  0表示获取第一页，获取更多的传1
     */
    public void getRoomNoticeList(String tag,boolean isShowLoading,String startid,String islater);


    /**
     * 获取我说的列表
     * @param typeToken
     * @param startId
     * @param action
     */
    public void getMySayMoreList(TypeToken typeToken,String startId,String action);
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


    public void setLiveid(String liveid);

    public void setSpeakid(String speakid);

    public void setMydataid(String mydataid);

    public void setTopicid(String topicid);

    public void setMarrowid(String marrowid);

    public void setCallid(String callid);





}
