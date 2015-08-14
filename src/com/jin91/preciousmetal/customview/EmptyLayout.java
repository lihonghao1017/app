package com.jin91.preciousmetal.customview;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.jin91.preciousmetal.R;

public class EmptyLayout {

    private Context mContext;
    private ViewGroup mLoadingView;
    private ViewGroup mEmptyView;
    private ViewGroup mErrorView;
    private Animation mLoadingAnimation;
    private ViewGroup viewGroup;
    private LayoutInflater mInflater;
    private View.OnClickListener mLoadingButtonClickListener;
    private View.OnClickListener mEmptyButtonClickListener;
    private View.OnClickListener mErrorButtonClickListener;

    // ---------------------------
    // static variables
    // ---------------------------
    /**
     * The empty state
     */
    public final static int TYPE_EMPTY = 1;
    /**
     * The loading state
     */
    public final static int TYPE_LOADING = 2;
    /**
     * The error state
     */
    public final static int TYPE_ERROR = 3;

    // ---------------------------
    // default values
    // ---------------------------
    private int mEmptyType = TYPE_LOADING;
    //    private String mErrorMessage = "网络不稳定，加载失败";
    private String mEmptyMessage;
    //    private String mLoadingMessage = "Please wait";
    //	private int mLoadingViewButtonId = R.id.buttonLoading;
//    private int mEmptyViewButtonId = R.id.buttonEmpty;
    View loadingAnimationView;
    private boolean mShowEmptyButton = true;
    private boolean mShowLoadingButton = true;
    private boolean mShowErrorButton = true;

    // ---------------------------
    // getters and setters
    // ---------------------------

    /**
     * Gets the loading layout
     *
     * @return the loading layout
     */
    public ViewGroup getLoadingView() {
        return mLoadingView;
    }

    /**
     * Sets loading layout
     *
     * @param loadingView the layout to be shown when the list is loading
     */
    public void setLoadingView(ViewGroup loadingView) {
        this.mLoadingView = loadingView;
    }

    /**
     * Sets loading layout resource
     *
     * @param res the resource of the layout to be shown when the list is loading
     */
    public void setLoadingViewRes(int res) {
        this.mLoadingView = (ViewGroup) mInflater.inflate(res, null);
    }

    /**
     * Gets the empty layout
     *
     * @return the empty layout
     */
    public ViewGroup getEmptyView() {
        return mEmptyView;
    }

    /**
     * Sets empty layout
     *
     * @param emptyView the layout to be shown when no items are available to load in the list
     */
    public void setEmptyView(ViewGroup emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * Sets empty layout resource
     *
     * @param res the resource of the layout to be shown when no items are available to load in the list
     */
    public void setEmptyViewRes(int res) {
        this.mEmptyView = (ViewGroup) mInflater.inflate(res, null);
    }

    /**
     * Gets the error layout
     *
     * @return the error layout
     */
    public ViewGroup getErrorView() {
        return mErrorView;
    }

    /**
     * Sets error layout
     *
     * @param errorView the layout to be shown when list could not be loaded due to some error
     */
    public void setErrorView(ViewGroup errorView) {
        this.mErrorView = errorView;
    }

    /**
     * Sets error layout resource
     *
     * @param res the resource of the layout to be shown when list could not be loaded due to some error
     */
    public void setErrorViewRes(int res) {
        this.mErrorView = (ViewGroup) mInflater.inflate(res, null);
    }

    /**
     * Gets the loading animation
     *
     * @return the loading animation
     */
    public Animation getLoadingAnimation() {
        return mLoadingAnimation;
    }

    /**
     * Sets the loading animation
     *
     * @param animation the animation to play when the list is being loaded
     */
    public void setLoadingAnimation(Animation animation) {
        this.mLoadingAnimation = animation;
    }

    /**
     * Sets the resource of loading animation
     *
     * @param animationResource the animation resource to play when the list is being loaded
     */
    public void setLoadingAnimationRes(int animationResource) {
        mLoadingAnimation = AnimationUtils.loadAnimation(mContext, animationResource);
    }

    /**
     * Gets the list view for which this library is being used
     * @return the list view
     */
//	public ListView getListView() {
//		return mListView;
//	}

    /**
     * Sets the list view for which this library is being used
     * @param listView
     */
//	public void setListView(ListView listView) {
//		this.mListView = listView;
//	}

    /**
     * Gets the last set state of the list view
     *
     * @return loading or empty or error
     */
    public int getEmptyType() {
        return mEmptyType;
    }

    /**
     * Sets the state of the empty view of the list view
     *
     * @param emptyType loading or empty or error
     */
    public void setEmptyType(int emptyType) {
        this.mEmptyType = emptyType;
        changeEmptyType();
    }


    /**
     * Gets the OnClickListener which perform when LoadingView was click
     *
     * @return
     */
    public View.OnClickListener getLoadingButtonClickListener() {
        return mLoadingButtonClickListener;
    }

    /**
     * Sets the OnClickListener to LoadingView
     *
     * @param loadingButtonClickListener OnClickListener Object
     */
    public void setLoadingButtonClickListener(View.OnClickListener loadingButtonClickListener) {
        this.mLoadingButtonClickListener = loadingButtonClickListener;
    }

    /**
     * Gets the OnClickListener which perform when EmptyView was click
     *
     * @return
     */
    public View.OnClickListener getEmptyButtonClickListener() {
        return mEmptyButtonClickListener;
    }

    /**
     * Sets the OnClickListener to EmptyView
     *
     * @param emptyButtonClickListener OnClickListener Object
     */
    public void setEmptyButtonClickListener(View.OnClickListener emptyButtonClickListener) {
        this.mEmptyButtonClickListener = emptyButtonClickListener;
    }

    /**
     * Gets the OnClickListener which perform when ErrorView was click
     *
     * @return
     */
    public View.OnClickListener getErrorButtonClickListener() {
        return mErrorButtonClickListener;
    }

    /**
     * Sets the OnClickListener to ErrorView
     *
     * @param errorButtonClickListener OnClickListener Object
     */
    public void setErrorButtonClickListener(View.OnClickListener errorButtonClickListener) {
        this.mErrorButtonClickListener = errorButtonClickListener;
    }

    /**
     * Gets if a button is shown in the empty view
     *
     * @return if a button is shown in the empty view
     */
    public boolean isEmptyButtonShown() {
        return mShowEmptyButton;
    }

    /**
     * Sets if a button will be shown in the empty view
     *
     * @param showEmptyButton will a button be shown in the empty view
     */
    public void setShowEmptyButton(boolean showEmptyButton) {
        this.mShowEmptyButton = showEmptyButton;
    }

    /**
     * Gets if a button is shown in the loading view
     *
     * @return if a button is shown in the loading view
     */
    public boolean isLoadingButtonShown() {
        return mShowLoadingButton;
    }

    /**
     * Sets if a button will be shown in the loading view
     * <p/>
     * //     * @param showEmptyButton will a button be shown in the loading view
     */
    public void setShowLoadingButton(boolean showLoadingButton) {
        this.mShowLoadingButton = showLoadingButton;
    }

    /**
     * Gets if a button is shown in the error view
     *
     * @return if a button is shown in the error view
     */
    public boolean isErrorButtonShown() {
        return mShowErrorButton;
    }

    /**
     * Sets if a button will be shown in the error view
     * <p/>
     * //     * @param showEmptyButton will a button be shown in the error view
     */
    public void setShowErrorButton(boolean showErrorButton) {
        this.mShowErrorButton = showErrorButton;
    }

    // ---------------------------
    // private methods
    // ---------------------------

    private void changeEmptyType() {

        setDefaultValues();

        // change empty type
        if (viewGroup != null) {

            switch (mEmptyType) {
                case TYPE_EMPTY:
                    viewGroup.removeAllViews();


                    if (mEmptyView != null) {
                        if (TextUtils.isEmpty(mEmptyMessage)) {
                           mEmptyMessage = mContext.getResources().getString(R.string.empty_message);
                        }
                        TextView tv = (TextView) mEmptyView.findViewById(R.id.textViewMessage);
                        tv.setText(mEmptyMessage);
                        viewGroup.addView(mEmptyView);
                    }
                    if (mLoadingView != null) {
                        if (loadingAnimationView != null && loadingAnimationView.getAnimation() != null)
                            loadingAnimationView.getAnimation().cancel();
                    }
                    break;
                case TYPE_ERROR:
                    viewGroup.removeAllViews();

                    if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                    if (mErrorView != null) {
                        viewGroup.addView(mErrorView);
                    }
                    if (mLoadingView != null) {
                        if (loadingAnimationView != null && loadingAnimationView.getAnimation() != null)
                            loadingAnimationView.getAnimation().cancel();
                    }
                    break;
                case TYPE_LOADING:
                    viewGroup.removeAllViews();
                    if (mLoadingView != null) {
                        viewGroup.addView(mLoadingView);
                        if (mLoadingAnimation != null && loadingAnimationView != null) {
                            loadingAnimationView.startAnimation(mLoadingAnimation);
                        } else if (loadingAnimationView != null) {
                            loadingAnimationView.startAnimation(getRotateAnimation());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setDefaultValues() {
        if (mEmptyView == null) {
            mEmptyView = (ViewGroup) mInflater.inflate(R.layout.view_empty, null);
            if (mEmptyButtonClickListener != null) {
                mEmptyView.findViewById(R.id.btn_empty_data).setOnClickListener(mEmptyButtonClickListener);
            }
        }
        if (mLoadingView == null) {
            mLoadingView = (ViewGroup) mInflater.inflate(R.layout.view_loading, null);
            loadingAnimationView = mLoadingView.findViewById(R.id.pb_loading);
        }
        if (mErrorView == null) {
            mErrorView = (ViewGroup) mInflater.inflate(R.layout.view_error, null);
            if (mErrorButtonClickListener != null) {
                mErrorView.setOnClickListener(mErrorButtonClickListener);
            }
        }
    }

    private static Animation getRotateAnimation() {
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        return rotateAnimation;
    }


    // ---------------------------
    // public methods
    // ---------------------------

    /**
     * Constructor
     *
     * @param context the context (preferred context is any activity)
     */
    public EmptyLayout(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Constructor
     *
     * @param context the context (preferred context is any activity)
     *                //     * @param listView the list view for which this library is being used
     */
    public EmptyLayout(Context context, ViewGroup viewGroup) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.viewGroup = viewGroup;
    }


    /**
     * Shows the empty layout if the list is empty
     */
    public void showEmpty() {
        this.mEmptyType = TYPE_EMPTY;
        changeEmptyType();
    }

    /**
     * Shows the empty layout if the list is empty
     */
    public void showEmpty(String message) {
        this.mEmptyType = TYPE_EMPTY;
        this.mEmptyMessage = message;
        changeEmptyType();
    }

    /**
     * Shows loading layout if the list is empty
     */
    public void showLoading() {
        this.mEmptyType = TYPE_LOADING;
        changeEmptyType();
    }

    /**
     * Shows error layout if the list is empty
     */
    public void showError() {
        this.mEmptyType = TYPE_ERROR;
        changeEmptyType();
    }

    /**
     * 隐藏加载的动画
     */
    public void hideLoadinAnim() {
        if (mLoadingView != null) {
            if (loadingAnimationView != null && loadingAnimationView.getAnimation() != null)
                loadingAnimationView.getAnimation().cancel();
        }
    }

}
