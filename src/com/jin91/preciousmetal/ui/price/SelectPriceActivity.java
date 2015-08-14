package com.jin91.preciousmetal.ui.price;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/20.
 * 选择行情
 */
public class SelectPriceActivity extends BaseActivity {
	  @ViewInject(R.id.tv_title_back)
	    public  TextView tv_title_back;
	    @ViewInject(R.id.tv_title_option)
	    public TextView tv_title_option;
	    @ViewInject(R.id.tv_title_title)
	    public TextView tv_title_title;

    public static void actionLaunch(Context context,int requestCode) {
        ((Activity)context).startActivityForResult(new Intent(context, SelectPriceActivity.class),requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_price_list);
        ViewUtils.inject(this);
        initialize();
    }

    @Override
    public String getRequestTag() {
        return "SelectPriceActivity";
    }

    @Override
    public void initialize() {
        tv_title_title.setText("选择行情");
    }

    @OnClick(R.id.tv_title_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
        }
    }
}
