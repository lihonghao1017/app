package com.jin91.preciousmetal.ui.price;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.PriceAdapter;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.ui.MainActivity;
import com.jin91.preciousmetal.util.MathUtil;

/**
 * Created by lijinhua on 2015/4/24.
 */
@SuppressLint("InflateParams")
public class ItemPricePage implements PriceAdapter.OnItemClickListener,
		OnClickListener {

	private Context mContext;
	private RecyclerView recyclerview;
	private List<Price> mList;
	private PriceAdapter adapter;
	private View view;
	private Button tianjiaBut;
	private Button bianjiBut;
	private Map<String, List<Price>> map;

	public ItemPricePage(Context mContext, ArrayList<Price> mList, String excode,Map<String, List<Price>> map,int tag) {
		this.mContext = mContext;
		this.mList = mList;
		this.map=map;
		view = LayoutInflater.from(mContext).inflate(R.layout.price_list, null);
		recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
		if ("ZIXUAN".equals(excode)) {
			if (mList != null && mList.size() < 1) {
				tianjiaBut = (Button) view.findViewById(R.id.tianjiazixuan);
				tianjiaBut.setVisibility(View.VISIBLE);
				tianjiaBut.setOnClickListener(this);
			} else {
				bianjiBut = (Button) view.findViewById(R.id.bianjizixuan);
				bianjiBut.setVisibility(View.VISIBLE);
				bianjiBut.setOnClickListener(this);
			}
		}
		if(tag==0){
			recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			adapter = new PriceAdapter(mList, mContext,false);
			recyclerview.addItemDecoration(new PriceDivider(mContext));
		}else if(tag==1){
			recyclerview.setLayoutManager(new android.support.v7.widget.GridLayoutManager(mContext, 3));
			adapter = new PriceAdapter(mList, mContext,true);
			view.findViewById(R.id.titles).setVisibility(View.GONE);
		}
		
		recyclerview.setAdapter(adapter);
		adapter.setOnItemClickListener(this);
	}

	public View getContentView() {
		return view;
	}

	@Override
	public void onItemClick(View view, int position) {
		Price price = mList.get(position);
		String increase = MathUtil
				.keep2Decimal((Double.parseDouble(price.last) - Double
						.parseDouble(price.lastclose)));
		Activity activity = (Activity) mContext;
		if (activity instanceof MainActivity) {
			// 进入行情请详情页
			PriceDetailActivity.actionLaunch(mContext, price.code, price.name,
					price.sell + "", increase);
		} else if (activity instanceof SelectPriceActivity) {
			// 选择行情activity
			Intent intent = new Intent();
			intent.putExtra("tradeCode", price.code);
			intent.putExtra("titleName", price.name);
			intent.putExtra("sellPrice", price.sell + "");
			intent.putExtra("increase", increase);
			activity.setResult(Activity.RESULT_OK, intent);
			activity.finish();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bianjizixuan:
			Intent intent2 = new Intent(mContext, ZiXuanActivity.class);
			Bundle b2=new Bundle();
			b2.putSerializable("data", (Serializable) map);
			intent2.putExtra("data", b2);
			mContext.startActivity(intent2);
			break;
		case R.id.tianjiazixuan:
			Intent intent = new Intent(mContext, ZiXuanActivity.class);
			Bundle b=new Bundle();
			b.putSerializable("data", (Serializable) map);
			intent.putExtra("data", b);
			mContext.startActivity(intent);
			break;

		default:
			break;
		}
	}

}
