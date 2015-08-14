/*
 * Copyright (c) 2013,北京欧乐吧技术有限公司 All rights reserved.
 * 
 * @文件名称 DrawableButton.java
 * 
 * @作者 rongrong.hao
 * 
 * @修改日期 2013-7-15
 */
package com.jin91.preciousmetal.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class DrawableButton extends RadioButton {

    private Drawable mButtonDrawable;
    private int mButtonResource;

    public DrawableButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableButton(Context context) {
        super(context);
    }

    /**
     * Set the background to a given Drawable, identified by its resource id.
     *
     * @param resid the resource id of the drawable to use as the background 
     */
    public void setButtonDrawable(int resid) {
        if (resid != 0 && resid == mButtonResource) {
            return;
        }

        mButtonResource = resid;

        Drawable d = null;
        if (mButtonResource != 0) {
            d = getResources().getDrawable(mButtonResource);
        }
        setButtonDrawable(d);
    }

    /**
     * Set the background to a given Drawable
     *
     * @param d The Drawable to use as the background
     */
    public void setButtonDrawable(Drawable d) {
        if (d != null) {
            if (mButtonDrawable != null) {
                mButtonDrawable.setCallback(null);
                unscheduleDrawable(mButtonDrawable);
            }
            d.setCallback(this);
            d.setState(getDrawableState());
            d.setVisible(getVisibility() == VISIBLE, false);
            mButtonDrawable = d;
            mButtonDrawable.setState(null);
            setMinHeight(mButtonDrawable.getIntrinsicHeight());
        }

        refreshDrawableState();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Drawable buttonDrawable = mButtonDrawable;
        if (buttonDrawable != null) {
            final int drawableHeight = buttonDrawable.getIntrinsicHeight();
            final int drawableWidth = buttonDrawable.getIntrinsicWidth();

            int top = (getHeight() - drawableHeight) / 2;
            int bottom = top + drawableHeight;
            int left = (getWidth() - drawableWidth) / 2;
            int right = left + drawableWidth;

            buttonDrawable.setBounds(left, top, right, bottom);
            buttonDrawable.draw(canvas);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mButtonDrawable != null) {
            int[] myDrawableState = getDrawableState();

            // Set the state of the Drawable
            mButtonDrawable.setState(myDrawableState);

            invalidate();
        }
    }

}
