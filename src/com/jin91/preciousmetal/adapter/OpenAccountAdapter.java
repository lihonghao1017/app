package com.jin91.preciousmetal.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jin91.preciousmetal.ui.mine.OpenAccountPage;

import java.util.List;

/**
 * Created by Administrator on 2015/5/2.
 */
public class OpenAccountAdapter extends PagerAdapter{


    private List<OpenAccountPage> mList;
    public OpenAccountAdapter(List<OpenAccountPage> mList)
    {
    this.mList = mList;
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager)container).removeView(mList.get(position).getContentView());
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(mList.get(position).getContentView(), 0);
        return mList.get(position).getContentView();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
