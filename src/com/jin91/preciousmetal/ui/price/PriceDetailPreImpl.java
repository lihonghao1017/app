package com.jin91.preciousmetal.ui.price;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.common.api.PriceApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.ListDialogEntity;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijinhua on 2015/5/19.
 */
public class PriceDetailPreImpl implements PriceDetailPre {

    private PriceDetailView view;

    public PriceDetailPreImpl(PriceDetailView view) {
        this.view = view;
    }

    @Override
    public void getPrice(String tag, final boolean isShowDialog, String code) {
        if (isShowDialog) {
            view.showLoading();
        }
        PriceApi.getPrice(tag, code, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                try {
                    TypeToken<List<Price>> type = new TypeToken<List<Price>>() {
                    };
                    List<Price> mList = JsonUtil.parse(json, type);
                    if (isShowDialog) {
                        view.hideLoading();
                    }
                    if (mList != null && mList.size() > 0) {
                        view.setPrice(mList.get(0));
                    }
                } finally {
                    view.priceKNetReturn();
                }
            }

            @Override
            public void onException(VolleyError e) {
                try {
                    if (isShowDialog) {
                        view.showToastNetErro();
                        view.hideLoading();
                    }

                } finally {
                    view.priceKNetReturn();
                }
            }
        });
    }

    @Override
    public void getPriceK(String tag, final boolean isShowDialog, String code, int time) {
        if (isShowDialog) {
            view.showLoading();
        }
        PriceApi.priceK(tag, code, time + "", new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                try {
                    JSONArray array = new JSONArray(json);
                    if (array == null || array.length() == 0 || array.length() < 5) {
                        view.showToast("暂不支持该行情");
                        view.hideLoading();
                        return;
                    }
                    TypeToken<List<Price>> type = new TypeToken<List<Price>>() {
                    };
                    List<Price> list = JsonUtil.parseList(json, type);
                    view.setPriceKList(list);
                    if (isShowDialog) {
                        view.hideLoading();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onException(VolleyError e) {
                if (isShowDialog) {
                    view.showToastNetErro();
                    view.hideLoading();
                }

            }
        });
    }

    @Override
    public void getPriceTime(String tag, final boolean isShowDialog, String code) {
        if (isShowDialog) {
            view.showLoading();
        }
        PriceApi.priceTime(tag, code, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                try {
                    JSONArray array = new JSONArray(json);
                    if (array == null || array.length() == 0 || array.length() < 5) {
                        view.showToast("暂不支持该行情");
                        view.hideLoading();
                        return;
                    }
                    TypeToken<List<Price>> type = new TypeToken<List<Price>>() {
                    };
                    List<Price> list = JsonUtil.parseList(json, type);
                    if (list != null && list.size() > 0) {
                        view.setPriceTimeList(list);
                    }
                    if (isShowDialog) {
                        view.hideLoading();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    view.priceTimeNetReturn();
                }
            }

            @Override
            public void onException(VolleyError e) {
                try {
                    if (isShowDialog) {
                        view.showToastNetErro();
                        view.hideLoading();
                    }

                } finally {
                    view.priceTimeNetReturn();
                }
            }
        });

    }


    @Override
    public List<ListDialogEntity> initCycleData(List<ListDialogEntity> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new ListDialogEntity("1分K", 1));
        list.add(new ListDialogEntity("3分K", 3));
        list.add(new ListDialogEntity("5分K", 5));
        list.add(new ListDialogEntity("10分K", 10));
        list.add(new ListDialogEntity("15分K", 15));
        list.add(new ListDialogEntity("30分K", 30));
        list.add(new ListDialogEntity("60分K", 60));
        list.add(new ListDialogEntity("90分K", 90));
        list.add(new ListDialogEntity("120分K", 120));
        list.add(new ListDialogEntity("180分K", 180));
        list.add(new ListDialogEntity("240分K", 240));
        list.add(new ListDialogEntity("日线", 1440));
        return list;
    }

    @Override
    public List<ListDialogEntity> initIndicatorData(List<ListDialogEntity> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new ListDialogEntity("平均线", 0));
        list.add(new ListDialogEntity("BOLL布林线", 1));
        list.add(new ListDialogEntity("MACD指标", 2));
        list.add(new ListDialogEntity("KDJ随机指标", 3));
        list.add(new ListDialogEntity("RSI强弱指标", 4));
        list.add(new ListDialogEntity("ADX指标", 5));
        list.add(new ListDialogEntity("ATR指标", 6));
        list.add(new ListDialogEntity("DMI指标", 7));
        
        return list;
    }


}
