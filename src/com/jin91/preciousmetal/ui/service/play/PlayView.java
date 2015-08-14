package com.jin91.preciousmetal.ui.service.play;

import com.jin91.preciousmetal.common.api.entity.AllData;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/13.
 */
public interface PlayView<T> extends CommonView<T>{
    /**
     * 显示Toast信息
     */
    public void showToastMessage(String message);

    /**
     * 显示处理的dialog
     */
    public void showProcessDialog();

    /**
     * 隐藏处理的dialog
     */
    public void hideProcessDialog();

    /**
     * 发送评论或者回复成功
     */
    public void sendSuccess();

    /**
     * 隐藏输入法
     */
    public void hideInputMethod();

    /**
     * 设置新的消息
     * @param allData
     */
    public void setNewMsgCount(AllData<T> allData);

    /**
     * 设置获取第一页的数据
     * @param list
     */
    public void setFistDataList(List<T> list);

    /**设置更多的数据
     * @param list
     */
    public void setMoreDataList(List<T> list);

    /**
     * 设置刷新完成
     */
    public void setRefreshComplete();

    /**
     * 在建议里面，设置为即时建议的备注
     * @param suggestNote
     */
    public void setSuggestNote(String suggestNote);
}
