package com.jin91.preciousmetal.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/4/23.
 */
public abstract class BaseActivity extends FragmentActivity{

    protected Context mContext;
  

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreciousMetalAplication.INSTANCE.requestQueue.cancelAll(getRequestTag());
    }

    public abstract String getRequestTag();
    /**
     * 初始化方法，在onCreate方法中调用
     */
    public abstract  void initialize();
}
