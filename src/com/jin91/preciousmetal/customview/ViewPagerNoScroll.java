package com.jin91.preciousmetal.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lijinhua on 2015/5/21.
 */
public class ViewPagerNoScroll extends ViewPager {
    public ViewPagerNoScroll(Context context) {
        super(context);
    }

    public ViewPagerNoScroll(Context context, AttributeSet attrs) {
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
