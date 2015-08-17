package com.jin91.preciousmetal.ui.price;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.PriceViewPageAdapter;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.customview.EmptyLayout;
import com.jin91.preciousmetal.customview.pagerindicator.TabPageIndicator;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.MainActivity;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/4/24.
 */
public class PriceFragment extends BaseFragment implements PriceView, View.OnClickListener {


    @ViewInject(R.id.fl_price_content)
    FrameLayout fl_price_content;
    @ViewInject(R.id.tab_pageindicator)
    TabPageIndicator tab_pageindicator;
    @ViewInject(R.id.view_pager)
    ViewPager view_pager;

    private List<Price> mList;
    EmptyLayout layout;
    private List<ItemPricePage> mListPages;
    private PricePresenter pricePresenter;
    PriceViewPageAdapter pagerAdapter;
    LinkedHashMap<String, String> category_map;

    private boolean isVisable;// 是否可见
    private boolean threadStart; // 线程是否开始
    private Handler handler;
    private Map<String, List<ItemPricePage>> map = new HashMap<>();

    public PriceFragment() {

    }

    public static PriceFragment newInstance() {
        PriceFragment fragment = new PriceFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.price_fragment, container, false);
        ViewUtils.inject(this, view);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisable = true;
        if (!threadStart) {
            threadStart = true;
            pricePresenter.getPriceList(MainActivity.TAG, "");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        isVisable = false;
    }

    /**
     * 继续获取行情
     */
    private void contiumePrice() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isVisable && !threadStart) {
                    threadStart = true;
                    pricePresenter.getPriceList(MainActivity.TAG, "");
                }
            }
        }, Constants.PRICE_INTERVAL);
    }

    @Override
    public void initialize() {
        pricePresenter = new PricePresenterImpl(this);
        mListPages = new ArrayList<ItemPricePage>();
        layout = new EmptyLayout(mContext, fl_price_content);
        layout.setErrorButtonClickListener(this);

        category_map = new LinkedHashMap<String, String>();
        category_map.put("ZIXUAN", "自选");
        category_map.put("TJPME", "天交所");
        category_map.put("SSY", "深油所");
        category_map.put("WGJS", "国际现货");
        category_map.put("SGE", "金交所");
        category_map.put("FOREX", "外汇");
        category_map.put("OIL", "国际原油");
        category_map.put("STOCKINDEX", "美股指数");
        category_map.put("SHQH", "上海期货");
        category_map.put("COMEX", "COMEX");
        pagerAdapter = new PriceViewPageAdapter(mListPages, getResources().getStringArray(R.array.trade_type));
        view_pager.setAdapter(pagerAdapter);
        tab_pageindicator.setViewPager(view_pager);
        view_pager.setCurrentItem(0);
        tab_pageindicator.setCurrentItem(0);

    }


    @SuppressWarnings("unchecked")
	@Override
    public void setItems(Map<String, List<Price>> map) {
        if (fl_price_content != null && fl_price_content.getVisibility() == View.INVISIBLE) {
            fl_price_content.setVisibility(View.GONE);
        }
    	SharedPreferences sp=mContext.getSharedPreferences("Price", Context.MODE_PRIVATE);
		String as=sp.getString("cave_price", null);
        Iterator<Entry<String, String>> iter = category_map.entrySet().iterator();
        mListPages.clear(); 
        
        List<ItemPricePage> newPage = new ArrayList<ItemPricePage>();
//        ArrayList<Price> zixuanPrices=new ArrayList<>();
        ArrayList<Price> zixuanPrices=new Gson().fromJson(as, new TypeToken<List<Price>>(){}.getType());  ;
        if(zixuanPrices==null){
        	zixuanPrices=new ArrayList<>();
        }
        while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			ArrayList<Price> prices= (ArrayList<Price>) map.get(entry.getKey());
			for (int i = 0; i < prices.size(); i++) {
				Price price=prices.get(i);
				if(as!=null&&as.contains(price.name)&&as.contains(price.excode)){
					for (int j = 0; j < zixuanPrices.size(); j++) {
						Price p=zixuanPrices.get(j);
						if(price.name.equals(p.name)&&price.excode.equals(p.excode)){
							p.setPrice(price);
						}
					}
				}
			}
			if(prices.size()>0)
            newPage.add(new ItemPricePage(mContext, prices, entry.getKey(),map,0));
        }
        newPage.add(0,new ItemPricePage(mContext, zixuanPrices, "ZIXUAN",map,0));
        mListPages.addAll(newPage);
        pagerAdapter.notifyDataSetChanged();
        if (tab_pageindicator != null) {
            tab_pageindicator.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshComplete() {
        threadStart = false;
        contiumePrice();
    }

    @Override
    public void showLoading() {
        layout.showLoading();
    }

    @Override
    public void hideLoading() {
        fl_price_content.setVisibility(View.GONE);
    }

    @Override
    public void showNetErrView() {
        layout.showError();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void setTitleNav() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.tv_title_title.setText(getString(R.string.price));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_view_error:
                pricePresenter.getPriceList(MainActivity.TAG, "");
                break;
        }
    }
}
