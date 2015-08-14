package com.jin91.preciousmetal.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.ui.price.ZiXuanActivity;
import com.jin91.preciousmetal.util.PriceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2015/4/25.
 * 行情适配器
 */
public class PriceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HIGH = 1; // 涨的布局变量
    public static final int LOW = 0; // 低的布局变量
    public static final int Grid=2; //自选
    private boolean isGrid;
    private List<Price> mList;
    private Context mContext;
    private PriceUtil priceUtil;
    public OnItemClickListener onItemClickListener;
    

    public PriceAdapter(List list, Context context,boolean isGrid) {
        this.mList = list;
        this.mContext = context;
        this.isGrid=isGrid;
        priceUtil = new PriceUtil();
    }

    @Override
    public int getItemViewType(int position) {
    	if(isGrid){
    		return Grid;
    	}
        Price price = mList.get(position);
        double increase = Double.parseDouble(price.last) - Double.parseDouble(price.lastclose);// 当前卖价
        if (increase < 0.0) {
            return LOW;
        } else {
            return HIGH;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LOW:
                return new LowViewHolder(LayoutInflater.from(mContext).inflate(R.layout.price_list_low_item, parent, false));
            case HIGH:
                return new HighViewHolder(LayoutInflater.from(mContext).inflate(R.layout.price_list_high_item, parent, false));
            case Grid:
            return new GridViewHolder(LayoutInflater.from(mContext).inflate(R.layout.zixuanitem, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Price price = mList.get(position);
        double increase = Double.parseDouble(price.last) - Double.parseDouble(price.lastclose); // 当前的价格-昨日的收盘价
        String increase_str = priceUtil.formatNum(increase, price.code);
        double increasePercent = ((Double.parseDouble(price.last) - Double.parseDouble(price.lastclose)) * 100) / Double.parseDouble(price.lastclose);
        String increase_percent_str = priceUtil.roundOff(2, increasePercent);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, holder.getPosition());
                }
            });
        }
        switch (getItemViewType(position)) {
            case LOW:
                LowViewHolder lowHolder = (LowViewHolder) holder;
                lowHolder.tv_name.setText(price.name);
                lowHolder.tv_sale.setText(priceUtil.formatNum(Double.parseDouble(price.sell), price.code));
                lowHolder.tv_buy.setText(priceUtil.formatNum(Double.parseDouble(price.buy), price.code));
                lowHolder.tv_high.setText(priceUtil.formatNum(Double.parseDouble(price.high), price.code));
                lowHolder.tv_low.setText(priceUtil.formatNum(Double.parseDouble(price.low), price.code));
                lowHolder.tv_size_num.setText(increase_str);
                lowHolder.tv_size_pre.setText(increase_percent_str + "%");
                break;
            case HIGH:
                HighViewHolder highHolder = (HighViewHolder) holder;
                highHolder.tv_name.setText(price.name);
                highHolder.tv_sale.setText(priceUtil.formatNum(Double.parseDouble(price.sell), price.code));
                highHolder.tv_buy.setText(priceUtil.formatNum(Double.parseDouble(price.buy), price.code));
                highHolder.tv_high.setText(priceUtil.formatNum(Double.parseDouble(price.high), price.code));
                highHolder.tv_low.setText(priceUtil.formatNum(Double.parseDouble(price.low), price.code));
                highHolder.tv_size_num.setText("+" + increase_str);
                highHolder.tv_size_pre.setText(increase_percent_str + "%");
                break;
            case Grid:
                GridViewHolder gridHolder = (GridViewHolder) holder;
                gridHolder.tv_name.setText(price.name);
                String json=((ZiXuanActivity)mContext).zixuanJson;
                if(json!=null&&json.contains(price.name)&&json.contains(price.code))
                	price.isCheched=true;
                gridHolder.tv_name.setBackgroundColor(price.isCheched?0xFFFF4500:0xFFFF83FA);
                gridHolder.tv_name.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (!price.isCheched) {
							price.isCheched=true;
							notifyDataSetChanged();
							((ZiXuanActivity)mContext).ziXuanList.add(price);
							((ZiXuanActivity)mContext).ziXuanAdapter.notifyDataSetChanged();
						}
					}
				});
                break;
        }

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickLitener) {
        this.onItemClickListener = mOnItemClickLitener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class HighViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.tv_name)
        public TextView tv_name;
        @ViewInject(R.id.tv_buy)
        public TextView tv_buy;
        @ViewInject(R.id.tv_sale)
        public TextView tv_sale;
        @ViewInject(R.id.tv_high)
        public TextView tv_high;
        @ViewInject(R.id.tv_low)
        public TextView tv_low;
        @ViewInject(R.id.tv_size_num)
        public TextView tv_size_num;
        @ViewInject(R.id.tv_size_pre)
        public TextView tv_size_pre;

        public HighViewHolder(View view) {
            super(view);
            ViewUtils.inject(this, view);
        }
    }

    public static class LowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @ViewInject(R.id.tv_name)
        public TextView tv_name;
        @ViewInject(R.id.tv_buy)
        public TextView tv_buy;
        @ViewInject(R.id.tv_sale)
        public TextView tv_sale;
        @ViewInject(R.id.tv_high)
        public TextView tv_high;
        @ViewInject(R.id.tv_low)
        public TextView tv_low;
        @ViewInject(R.id.tv_size_num)
        public TextView tv_size_num;
        @ViewInject(R.id.tv_size_pre)
        public TextView tv_size_pre;

        public LowViewHolder(View view) {
            super(view);
            ViewUtils.inject(this, view);
        }

        @Override
        public void onClick(View v) {
        }
    }
    public static class GridViewHolder extends RecyclerView.ViewHolder  {
        @ViewInject(R.id.ziXuanItem)
        public TextView tv_name;

        public GridViewHolder(View view) {
            super(view);
            ViewUtils.inject(this, view);
        }

      
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
