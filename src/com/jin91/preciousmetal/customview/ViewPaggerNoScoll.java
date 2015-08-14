package com.jin91.preciousmetal.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lijinhua on 2015/4/23.
 * 自定义不可滑动的viewpager
 */
public class ViewPaggerNoScoll extends ViewPager{

    public ViewPaggerNoScoll(Context context) {
        super(context);
    }

    public ViewPaggerNoScoll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
