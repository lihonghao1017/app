package com.jin91.preciousmetal.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by lijinhua on 2015/4/23.
 */
public abstract class BaseFragment extends Fragment {


    protected Context mContext;

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();

    }

    /**
     * 初始化方法，在onCreateView方法中调用
     */
    public abstract  void initialize();

    /**
     *设置title的导航
     */
    public abstract  void setTitleNav();


}
