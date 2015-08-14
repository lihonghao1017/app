package com.jin91.preciousmetal.ui.service;

import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.common.api.entity.Room;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/12.
 */
public interface ServiceView {

    public void setItem(List<Room> list);

    public void setPriceNotice(Price price);

    public void setDirectPlay(AllData<DirectPlay> playAllData);

    /**
     * 网络错误
     */
    public void netError();

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
    public void sendCommSuccess();

    public void sendDirectSuccess();
    /**
     * 隐藏输入法
     */
    public void hideInputMethod();
}
