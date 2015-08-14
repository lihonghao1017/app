package com.jin91.preciousmetal.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jin91.preciousmetal.ui.price.ItemPricePage;

import java.util.List;

/**
 * Created by lijinhua on 2015/4/27.
 * 行情的viewpage
 */
public class PriceViewPageAdapter extends PagerAdapter {


    private List<ItemPricePage> mListPages;
    private  String[] tradeType;

    public PriceViewPageAdapter(List<ItemPricePage> mListPages, String[] tradeType) {
        this.mListPages = mListPages;
        this.tradeType = tradeType;
    }

    @Override
    public int getCount() {
        return mListPages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListPages.get(position).getContentView());
        return mListPages.get(position).getContentView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tradeType[position];
    }
}
