package com.jin91.preciousmetal.ui.price;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2015/4/26.
 * 行情适配器的一个分隔线
 */
public class PriceDivider  extends RecyclerView.ItemDecoration{

    // 系统默认分隔符资源的ID
    private static final int[] ATTRS = {android.R.attr.listDivider};
    // 分隔条Drawable对象
    private Drawable mDivider;
    // 构造函数初始化
    public PriceDivider(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        // 获得系统提供的分隔条Drawable对象
        mDivider = typedArray.getDrawable(0);
        // 回收typedArray所占用的资源
        typedArray.recycle();
    }

    // 绘制所有列表项之间的分隔条
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        // 获取列表项距离左边缘的距离
        int left = parent.getPaddingLeft();
        // 获取列表项距离右边缘的距离
        int right = parent.getWidth() - parent.getPaddingRight();
        // 获取列表项的总数
        int childCount = parent.getChildCount();

        // 绘制所有的列表项之间的分割线
        for (int i = 0; i < childCount; i++) {
            // 获得当前的列表项
            View child = parent.getChildAt(i);

            // 获得当前列表项的布局参数信息
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            // 计算分隔条左上角的纵坐标
            int top = child.getBottom() + params.bottomMargin;

            // 计算分隔条右下角的纵坐标
            int bottom = top + mDivider.getIntrinsicHeight();

            // 设置分隔条的绘制范围
            mDivider.setBounds(left, top, right, bottom);

            // 开始绘制当前列表项下面的分隔条
            mDivider.draw(c);

        }
    }

}
