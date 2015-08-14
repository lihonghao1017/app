package com.jin91.preciousmetal.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.util.Logger;

/**
 * Created by lijinhua on 2015/4/28.
 */
public class ExpandListView extends ListView implements AbsListView.OnScrollListener, android.widget.AdapterView.OnItemClickListener {

    private static final String TAG = "RefreshListView";
    private View footView;
    private int footViewHeight;
    private ImageView foot_arrowImageView;
    private ProgressBar foot_progressBar;
    private TextView foot_tipsTextView;
    private Animation upAnimation; // 向上旋转的动画
    private Animation downAnimation; // 向下旋转的动画
    private Boolean isEnableFoot; // 是否启用底部刷新

    private final int UP_PULL_REFRESH = 0; // 上拉刷新状态
    private final int RELEASE_REFRESH = 1; // 松开刷新
    private final int REFRESHING = 2; // 正在刷新中
    private final int DONE = 3;
    private int currentState = UP_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态
    private int downY; // 按下时y轴的偏移量
    private int lastItemIndex;
    private int totalItemCount;
    private OnRefreshListener onRefreshListener;
    private boolean isGtFristPage; // 数据是否大于一页
//    private boolean isDoodleDetail = true;// 是否是涂压详情页

    public ExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooterView();
    }

    private void initFooterView() {
        footView = View.inflate(getContext(), R.layout.listview_footer, null);
        foot_arrowImageView = (ImageView) footView.findViewById(R.id.head_arrowImageView);
        foot_progressBar = (ProgressBar) footView.findViewById(R.id.head_progressBar);
        foot_tipsTextView = (TextView) footView.findViewById(R.id.foot_tipsTextView);
        footView.measure(0, 0);
        footViewHeight = footView.getMeasuredHeight();
        //        Log.i(TAG, "测量后的高度: " + footViewHeight); // 62
        footView.setPadding(0, -footViewHeight, 0, 0);
        addFooterView(footView, null, false);
//        setOnScrollListener(new PauseOnScrollListener(BitmapLoader.getInstance().getImageLoader(), true, false, this));
        setOnScrollListener(this);
        setOnItemClickListener(this);
        initAnimation();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnableFoot) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                Logger.i(TAG, "isGtFristPage===" + isGtFristPage);
                   Logger.i(TAG, "getLastVisiblePosition()===" + getLastVisiblePosition() + "===totalItemCount===" + totalItemCount);
                   Logger.i(TAG, "lastItemIndex" + lastItemIndex + "===totalItemCount===" + totalItemCount);
                    if (currentState != REFRESHING && lastItemIndex == totalItemCount && isGtFristPage) {
                        int moveY = (int) ev.getY();
                        if (downY == 0) {
                            downY = moveY;
                        }
                        int paddingButtom = (downY - moveY) / 2 - footViewHeight;

                        if (currentState == DONE) {
                            currentState = UP_PULL_REFRESH;
                            refreshFootView();
                        }
                        if (currentState == UP_PULL_REFRESH) {
                            setSelection(lastItemIndex);
                            if (paddingButtom >= 0) {
                                currentState = RELEASE_REFRESH;
                                refreshFootView();
                            }
                        }
                        if (currentState == RELEASE_REFRESH) {
                            setSelection(lastItemIndex);
                            if (paddingButtom <= 0) {
                                currentState = UP_PULL_REFRESH;
                                refreshFootView();
                            }
                        }
                        Logger.i(TAG, "refreing==paddingButtom===="+paddingButtom);
                        footView.setPadding(0, 0, 0, paddingButtom);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // 判断当前的状态是松开刷新还是下拉刷新
                    if (currentState == RELEASE_REFRESH) {
                        // 把头布局设置为完全显示状态
                        // 进入到正在刷新中状态
                        currentState = REFRESHING;
                        refreshFootView();
                        footView.setPadding(0, 0, 0, 0);
                        this.setSelection(this.getCount());
                        if (onRefreshListener != null) {
                            onRefreshListener.onFootRefresh();
                        }
                    } else if (currentState == UP_PULL_REFRESH) {
                        // 隐藏头布局
                        footView.setPadding(0, -footViewHeight, 0, 0);
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据currentState刷新未布局的状态
     */
    private void refreshFootView() {
        switch (currentState) {
            case UP_PULL_REFRESH:
                foot_progressBar.setVisibility(View.GONE);
                foot_arrowImageView.setVisibility(View.VISIBLE);
                foot_tipsTextView.setText("上拉刷新");
                foot_arrowImageView.clearAnimation();
                foot_arrowImageView.startAnimation(upAnimation);
                break;
            case RELEASE_REFRESH:
                foot_progressBar.setVisibility(View.GONE);
                foot_arrowImageView.setVisibility(View.VISIBLE);
                foot_tipsTextView.setText("松开刷新");
                foot_arrowImageView.clearAnimation();
                foot_arrowImageView.startAnimation(downAnimation);
                break;
            case REFRESHING:
                foot_arrowImageView.clearAnimation();
                foot_progressBar.setVisibility(View.VISIBLE);
                foot_arrowImageView.setVisibility(View.GONE);
                foot_tipsTextView.setText("正在加载");
                break;
            default:
                break;
        }
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {

        upAnimation = new RotateAnimation(-180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

        downAnimation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
    }

    public interface OnRefreshListener {

        public void onFootRefresh();

        public void onScrollListener(int firstitem, int visibleitem, int totalitem);

        public void onItemClickListener(AdapterView<?> parent, View view, int position, long id);

        public void onScrollStateChanged(AbsListView view, int scrollState);
    }

    /**
     * 设置刷新底部完成
     */
    public void setRefreshFootComplet() {
        footView.setPadding(0, -footViewHeight, 0, 0);
        foot_progressBar.setVisibility(View.GONE);
        foot_arrowImageView.setVisibility(View.VISIBLE);
        foot_tipsTextView.setText("上拉刷新");
        currentState = UP_PULL_REFRESH;
    }

    /**设置listener监听
     * @param onRefreshListener
     * @param isEnableFoot 是否启用底部的上拉刷新
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener, boolean isEnableFoot) {
        this.onRefreshListener = onRefreshListener;
        this.isEnableFoot = isEnableFoot;
    }

    /**
     * 设置是否启用底部刷新
     * @param isEnableFoot
     */
    public void setIsEnableFoot(boolean isEnableFoot)
    {
        this.isEnableFoot = isEnableFoot;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onRefreshListener != null) {
            onRefreshListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItemIndex = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
        if (onRefreshListener != null) {
            onRefreshListener.onScrollListener(firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (totalItemCount > visibleItemCount && totalItemCount>1) {
            isGtFristPage = true;
        } else {
//            if (!isDoodleDetail) {
                isGtFristPage = false;
//            }
        }
        Logger.i(TAG, "totalItemCount===" + totalItemCount + "===visibleItemCount==" + visibleItemCount);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onRefreshListener != null) {
            onRefreshListener.onItemClickListener(parent, view, position, id);
        }
    }

//    public void setIsDoodleDetail(boolean notDoodleDetail) {
//        this.isDoodleDetail = notDoodleDetail;
//    }

}
