package com.jin91.preciousmetal.ui.price;

import com.jin91.preciousmetal.common.api.entity.Price;

import java.util.List;

/**
 * Created by lijinhua on 2015/4/24.
 */
public  interface   PricePresenter  {

    /**
     * 得到行情的数据
     */
    public   void getPriceList(String tag,String code);

}
