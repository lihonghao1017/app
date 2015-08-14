package com.jin91.preciousmetal.customview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.ui.base.BaseFragment;

import java.util.List;

public class StickyNavLayout extends LinearLayout {


    public static final String TAG = "StickyNavLayout";
    private View mTop;
    private View mNav;
    private LazyViewPager mViewPager;

    private int mTopViewHeight;
    private ViewGroup mInnerScrollView;
    private boolean isTopHidden = false;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private float mLastY;
    private boolean mDragging;
    private List<BaseFragment> mList;
    private int bottomViewHeight;
    private int bottom = 1;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        View bottomView = View.inflate(context, R.layout.direct_play_comment_layout, null);
        bottomView.measure(0, 0);
        bottomViewHeight = bottomView.getMeasuredHeight();
        Logger.i(TAG, "bottomViewHeight---" + bottomViewHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.id_stickynavlayout_topview);
        mNav = findViewById(R.id.id_stickynavlayout_indicator);
        View view = findViewById(R.id.id_stickynavlayout_viewpager);
        if (!(view instanceof LazyViewPager)) {
            throw new RuntimeException("id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (LazyViewPager) view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight() - bottomViewHeight * bottom;
        Logger.i(TAG, " getMeasuredHeight()---" + getMeasuredHeight() + "----mNav.getMeasuredHeight()---" + mNav.getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;

                getCurrentScrollView();

                if (Math.abs(dy) > mTouchSlop) {
                    mDragging = true;

                    if (mInnerScrollView instanceof ScrollView) {
                        if (!isTopHidden || (mInnerScrollView.getScrollY() == 0 && isTopHidden && dy > 0)) {
                            return true;
                        }
                    } else if (mInnerScrollView instanceof ListView) {
                        ListView lv = (ListView) mInnerScrollView;
                        View c = lv.getChildAt(lv.getFirstVisiblePosition());
                        try {
                            Logger.i(TAG, "getScrollY===" + getScrollY() + "===lv.getTop()===" + lv.getTop() + "----lv.getFirstVisiblePosition()--" + lv.getFirstVisiblePosition() + "dy===" + dy);
                            if (c != null) {
                                Logger.i(TAG, "isTopHidden===" + isTopHidden + "==c!=null====c.getTop()==" + c.getTop() + "==dy==" + dy + "==mNav.getTop==" + mNav.getTop());
                            } else {
                                Logger.i(TAG, "isTopHidden===" + isTopHidden + "==c==null==" + "==dy==" + dy + "==mNav.getTop==" + mNav.getTop());
                            }

                        } catch (Exception e) {
                        }
                        if (isTopHidden && lv.getFirstVisiblePosition() == 0 && dy > 0) {
                            // 顶部隐藏   listview第一个可见的view==0  dy大于0表示向下滑
                            return true;
                        } else if (!isTopHidden && lv.getFirstVisiblePosition() == 0 && dy < 0) {
                            return true;
                        }

                    }
                    break;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void getCurrentScrollView() {

        try {

            int currentItem = mViewPager.getCurrentItem();
            PagerAdapter a = mViewPager.getAdapter();
            if (a instanceof FragmentPagerAdapter) {
                FragmentPagerAdapter fadapter = (FragmentPagerAdapter) a;
                Fragment item = fadapter.getItem(currentItem);
                mInnerScrollView = (ViewGroup) (item.getView().findViewById(R.id.id_stickynavlayout_innerscrollview));
            } else if (a instanceof PagerAdapter) {
//                FragmentStatePagerAdapter fsAdapter = (FragmentStatePagerAdapter) a;
//                Fragment item = fsAdapter.getItem(currentItem);
//                mInnerScrollView = (ViewGroup) (item.getView().findViewById(R.id.id_stickynavlayout_innerscrollview));
                View view = mList.get(currentItem).getView();
                if (view != null) {
                    mInnerScrollView = (ViewGroup) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                mLastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;

                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }
                if (mDragging) {
                    scrollBy(0, (int) -dy);
                    mLastY = y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                mVelocityTracker.clear();
                break;
        }

        return super.onTouchEvent(event);
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }

        isTopHidden = getScrollY() == mTopViewHeight;

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public void setmList(List<BaseFragment> list) {
        this.mList = list;
    }

    public void setBottomViewHeight(int height) {
        bottom = height;
    }

}
