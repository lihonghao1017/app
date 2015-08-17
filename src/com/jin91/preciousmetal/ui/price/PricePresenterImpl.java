package com.jin91.preciousmetal.ui.price;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.common.api.PriceApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.util.JsonUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lijinhua on 2015/4/24.
 */
public class PricePresenterImpl implements PricePresenter {


    PriceView priceView;
    private boolean isSuccess;// 是否加载成功
    SharedPreferences.Editor editor;

    public PricePresenterImpl(PriceView priceView) {
        this.priceView = priceView;
        editor = PreciousMetalAplication.getContext().getSharedPreferences("price", Context.MODE_PRIVATE).edit();
    }

    @Override
    public void getPriceList(String tag, String code) {
        if (!isSuccess) {
            priceView.showLoading();
        }
        PriceApi.getPrice(tag, code, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                try {
                    TypeToken<List<Price>> type = new TypeToken<List<Price>>() {
                    };
                    List<Price> list = JsonUtil.parseList(json, type);
                    Map<String, List<Price>> map = new LinkedHashMap<String, List<Price>>();
                    for (Price price : list) {
                        if (!isSuccess) {
                            // 第一次的时候将code与名字对应，在报价提醒的时候用到
                            editor.putString(price.code, price.name);
                        }
                        //自选的价格信息
                        List<Price> zixuanList = map.get("ZIXUAN");
                        if (zixuanList == null) {
                        	zixuanList = new ArrayList<Price>();
                        }
                        //zixuanList.add(price);
                        map.put("ZIXUAN", zixuanList);
                        // 这里面保存
                        switch (price.excode) {
                            case "TJPME": // 天交所
                                List<Price> tjpmeList = map.get("TJPME");
                                if (tjpmeList == null) {
                                    tjpmeList = new ArrayList<Price>();
                                }
                                tjpmeList.add(price);
                                map.put("TJPME", tjpmeList);
                                break;
                            case "SSY": // 天交所
                                List<Price> ssyList = map.get("SSY");
                                if (ssyList == null) {
                                	ssyList = new ArrayList<Price>();
                                }
                                ssyList.add(price);
                                map.put("SSY", ssyList);
                                break;
                            case "WGJS": // 国际现货
                                List<Price> wgjsList = map.get("WGJS");
                                if (wgjsList == null) {
                                    wgjsList = new ArrayList<Price>();
                                }
                                wgjsList.add(price);
                                map.put("WGJS", wgjsList);
                                break;
                            case "SGE": // 金交所
                                List<Price> sgeList = map.get("SGE");
                                if (sgeList == null) {
                                    sgeList = new ArrayList<Price>();
                                }
                                sgeList.add(price);
                                map.put("SGE", sgeList);
                                break;
                            case "SHQH": // 上海期货
                                List<Price> shqhkList = map.get("SHQH");
                                if (shqhkList == null) {
                                    shqhkList = new ArrayList<Price>();
                                }
                                shqhkList.add(price);
                                map.put("SHQH", shqhkList);
                                break;
                            case "COMEX": // COMEX
                                List<Price> comexkList = map.get("COMEX");
                                if (comexkList == null) {
                                    comexkList = new ArrayList<Price>();
                                }
                                comexkList.add(price);
                                map.put("COMEX", comexkList);
                                break;
                            case "OIL": // 国际原油
                                List<Price> oitList = map.get("OIL");
                                if (oitList == null) {
                                    oitList = new ArrayList<Price>();
                                }
                                oitList.add(price);
                                map.put("OIL", oitList);
                                break;
                            case "STOCKINDEX": // 美股指数
                                List<Price> stockList = map.get("STOCKINDEX");
                                if (stockList == null) {
                                    stockList = new ArrayList<Price>();
                                }
                                stockList.add(price);
                                map.put("STOCKINDEX", stockList);
                                break;
                            case "FOREX": // 外汇
                                List<Price> forexList = map.get("FOREX");
                                if (forexList == null) {
                                    forexList = new ArrayList<Price>();
                                }
                                forexList.add(price);
                                map.put("FOREX", forexList);
                                break;
                        }
                    }
                    if (!isSuccess) {
                        editor.commit();
                    }
                    priceView.setItems(map);
                    isSuccess = true;
                    if (isSuccess) {
                        // 加载成功, 隐藏loading加载
                        priceView.hideLoading();
                    }
                } finally {
                    priceView.refreshComplete();
                }

            }

            @Override
            public void onException(VolleyError e) {
                if (!isSuccess) {
                    // 加载失败
                    priceView.showNetErrView();
                }
                priceView.refreshComplete();
            }
        });
    }


}
