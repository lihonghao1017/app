package com.jin91.preciousmetal.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.jin91.preciousmetal.R;


/**
 * 角标
 */
public class DrawableSubscriptNavButton extends DrawableButton {

    private static final String TAG = DrawableSubscriptNavButton.class.getSimpleName();
    private boolean subscriptAvaliable;
    private String mSubscriptText;
    private Drawable mSubscriptDrawable;
    private int mSubscriptResource;
    private Rect mSubscriptDrawablePadding = new Rect();
    private Paint mSubscriptPaint = new Paint();

    {
        mSubscriptPaint.setTextAlign(Align.CENTER);
        mSubscriptPaint.setAntiAlias(true);
        mSubscriptPaint.setColor(Color.WHITE);
        mSubscriptPaint.setTextSize(18);
    }

    public DrawableSubscriptNavButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DrawableSubscriptNavButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawableSubscriptNavButton(Context context) {
        super(context);
        init();
    }

    private void init() {
    }

    public void setSubscriptText(int text) {
        setGravity(Gravity.CENTER);
        mSubscriptText = text + "";
        if ("0".equals(mSubscriptText)) {
            hideSubscript();
        } else {
            if (!subscriptAvaliable) {
                subscriptAvaliable = true;
            }
            if (text > 99) {
                mSubscriptText = getContext().getString(R.string.N);
            }
            invalidate();
        }
    }

    public void setSubscriptTextSize(int textSize) {
        mSubscriptPaint.setTextSize(textSize);
    }

    public void setSubscriptTextColor(int color) {
        mSubscriptPaint.setColor(color);
    }

    public void setSubscriptDrawable(int resid) {
        if (resid != 0 && resid == mSubscriptResource) {
            return;
        }

        mSubscriptResource = resid;

        Drawable d = null;
        if (mSubscriptResource != 0) {
            d = getResources().getDrawable(mSubscriptResource);
        }
        setSubscriptDrawable(d);
    }

    public void setSubscriptDrawable(Drawable d) {
        if (d != null) {
            if (mSubscriptDrawable != null) {
                mSubscriptDrawable.setCallback(null);
                unscheduleDrawable(mSubscriptDrawable);
            }
            d.setCallback(this);
            d.setState(getDrawableState());
            d.setVisible(getVisibility() == VISIBLE, false);
            mSubscriptDrawable = d;
            mSubscriptDrawable.setState(null);
        }

        refreshDrawableState();
    }

    public void showSubscript() {
        if (!subscriptAvaliable) {
            subscriptAvaliable = true;
            invalidate();
        }
    }

    public void hideSubscript() {
        if (subscriptAvaliable) {
            subscriptAvaliable = false;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        final Drawable subscriptDrawable = mSubscriptDrawable;
        if (subscriptDrawable != null && subscriptAvaliable) {
            final int drawableHeight = subscriptDrawable.getIntrinsicHeight(); // 得到图片的高度
            final int drawableWidth = subscriptDrawable.getIntrinsicWidth(); // 得到图片的宽度
            int width = getWidth(); // 得到view的宽度
            int height = getHeight(); // 得到view的高度

            int top = 0;
            int bottom = top + drawableHeight;
            int left = width - drawableWidth;
            int right = width;

            left = Math.max(0, left);
//            left = Math.min(left, centerX);


            left = Math.max(left, centerX);
            bottom = Math.min(height, bottom);
            bottom = Math.max(bottom, centerY);

            right = left + drawableWidth;
            top = bottom - drawableHeight;
//            Drawable[] drawables = getCompoundDrawables();
//            if(drawables!=null && drawables.length>0)
//            {
//                Drawable drawableTop = drawables[1];
//                int drawLeft = (getWidth()-drawableTop.getIntrinsicWidth())/2+drawableTop.getIntrinsicWidth();
//                left = drawLeft;
//                right = left + drawableWidth;
//                top = getPaddingTop();
//                bottom = top+drawableHeight;
//            }

            subscriptDrawable.setBounds(left, top, right, bottom);
            subscriptDrawable.draw(canvas);
            subscriptDrawable.getPadding(mSubscriptDrawablePadding);

            final String subscriptText = mSubscriptText;
            if (subscriptText != null) {
                FontMetrics fontMetrics = mSubscriptPaint.getFontMetrics();
                final float textHeight = fontMetrics.bottom - fontMetrics.top;
                float textLeft = left + 1.0f * (right - left) / 2 + mSubscriptDrawablePadding.left - mSubscriptDrawablePadding.right;
//                float textTop = 1.0f * (bottom - top) / 2 + mSubscriptDrawablePadding.top - mSubscriptDrawablePadding.bottom - fontMetrics.top - textHeight / 2;
                // 文字的高度
                // (1.0f * (bottom - top) / 2 -textHeight)/2+ mSubscriptDrawablePadding.top  这个是图标的高度
                float textTop = top + 1.0f * (bottom - top) / 2 + mSubscriptDrawablePadding.top - mSubscriptDrawablePadding.bottom - fontMetrics.top - textHeight / 2;
                canvas.drawText(subscriptText, textLeft, textTop, mSubscriptPaint);


//                if (BuildConfig.DEBUG) {
//                    Logger.d(TAG, String.format("mSubscriptText textLeft:%s, textTop:%s, text ascent:%s,descent:%s", textLeft, textTop, mSubscriptPaint.ascent(), mSubscriptPaint.descent()));
//                }
            }
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mSubscriptDrawable != null) {
            int[] myDrawableState = getDrawableState();

            // Set the state of the Drawable
            mSubscriptDrawable.setState(myDrawableState);

            if (subscriptAvaliable) {
                invalidate();
            }
        }
    }

}
