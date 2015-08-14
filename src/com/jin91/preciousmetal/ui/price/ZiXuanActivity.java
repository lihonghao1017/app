package com.jin91.preciousmetal.ui.price;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.PriceViewPageAdapter;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.customview.pagerindicator.TabPageIndicator;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ZiXuanActivity extends Activity implements OnClickListener ,OnItemClickListener{

	private GridView gridView;
	public GridViewAdapter ziXuanAdapter;
	public ArrayList<Price> ziXuanList;
	private Map<String, List<Price>> map;
	private List<ItemPricePage> mListPages;
	private PriceViewPageAdapter pagerAdapter;
	private LinkedHashMap<String, String> category_map;
	@ViewInject(R.id.tab_pageindicator)
	TabPageIndicator tab_pageindicator;
	@ViewInject(R.id.zixuan_pager)
	ViewPager view_pager;
	@ViewInject(R.id.tv_title_back)
	public TextView tv_title_back;
	@ViewInject(R.id.tv_title_option)
	public TextView tv_title_option;
	@ViewInject(R.id.tv_title_title)
	public TextView tv_title_title;
	public String zixuanJson;

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zixuangridview);
		ViewUtils.inject(this);
		tv_title_title.setText("编辑自选");
		tv_title_option.setText("保存");
		tv_title_option.setOnClickListener(this);
		tv_title_back.setOnClickListener(this);
		map = (Map<String, List<Price>>) getIntent().getBundleExtra("data")
				.getSerializable("data");
		SharedPreferences sp=getSharedPreferences("Price", Context.MODE_PRIVATE);
		zixuanJson=sp.getString("cave_price", null);
        
        ziXuanList=new Gson().fromJson(zixuanJson, new TypeToken<List<Price>>(){}.getType());  ;
		if(ziXuanList==null){
			ziXuanList = new ArrayList<>();
		}
        gridView = (GridView) findViewById(R.id.ZiXuanGrid);
        gridView.setOnItemClickListener(this);
		ziXuanAdapter = new GridViewAdapter(this, ziXuanList);
		gridView.setAdapter(ziXuanAdapter);

		mListPages = new ArrayList<ItemPricePage>();
		category_map = new LinkedHashMap<String, String>();
		category_map.put("TJPME", "天交所");
		category_map.put("WGJS", "国际现货");
		category_map.put("SGE", "金交所");
		category_map.put("FOREX", "外汇");
		category_map.put("OIL", "国际原油");
		category_map.put("STOCKINDEX", "美股指数");
		category_map.put("SHQH", "上海期货");
		category_map.put("COMEX", "COMEX");
		String[] asArray = { "天交所", "国际现货", "金交所", "外汇", "国际原油", "美股指数",
				"上海期货", "COMEX" };
		pagerAdapter = new PriceViewPageAdapter(mListPages, asArray);

		view_pager.setAdapter(pagerAdapter);
		tab_pageindicator.setViewPager(view_pager);
		view_pager.setCurrentItem(0);
		tab_pageindicator.setCurrentItem(0);

		Iterator<Entry<String, String>> iter = category_map.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
					.next();
			ArrayList<Price> itemList = (ArrayList<Price>) map.get(entry
					.getKey());
			for (int i = 0; i < itemList.size(); i++) {
				if (itemList.get(i).ischecked)
					ziXuanList.add(itemList.get(i));
			}
			mListPages.add(new ItemPricePage(this, itemList, entry.getKey(),
					map, 1));
		}
		pagerAdapter.notifyDataSetChanged();
		tab_pageindicator.notifyDataSetChanged();
		ziXuanAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_back:
			finish();
			break;
		case R.id.tv_title_option:
			SharedPreferences sp = getSharedPreferences("Price",
					Context.MODE_PRIVATE);
			String as = new Gson().toJson(ziXuanList);
			sp.edit().putString("cave_price", as).commit();
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ziXuanList.remove(position);
		ziXuanAdapter.notifyDataSetChanged();
	}

}
