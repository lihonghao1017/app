package com.jin91.preciousmetal.ui.price;

import com.jin91.preciousmetal.common.api.entity.ListDialogEntity;

import java.util.List;

/**
 * Created by lijinhua on 2015/5/19.
 */
public interface PriceDetailPre {


    public List<ListDialogEntity> initCycleData(List<ListDialogEntity> list);

    public List<ListDialogEntity> initIndicatorData(List<ListDialogEntity> list);

    public void getPrice(String tag, boolean isShowDialog, String code);

    public void getPriceK(String tag, boolean isShowDialog, String code, int time);

    /**
     * 获取行情的分时图
     * @param tag
     * @param isShowDialog
     * @param code
     */
    public void getPriceTime(String tag, boolean isShowDialog, String code);
}
