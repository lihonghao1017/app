package com.jin91.preciousmetal.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.IntentUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/5.
 * 关于国鑫
 */
public class AboutJinActivity extends BaseActivity {

	  @ViewInject(R.id.tv_title_back)
	    public  TextView tv_title_back;
	    @ViewInject(R.id.tv_title_option)
	    public TextView tv_title_option;
	    @ViewInject(R.id.tv_title_title)
	    public TextView tv_title_title;
    @ViewInject(R.id.tv_preciousmetal_version)
    TextView tv_preciousmetal_version;

    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, AboutJinActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_about_jin);
        ViewUtils.inject(this);
        initialize();
    }

    @OnClick({R.id.tv_title_back, R.id.tv_about_callphone, R.id.tv_about_net, R.id.tv_about_email})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.tv_about_callphone:
                IntentUtil.callPhone(mContext, getString(R.string.official_tel));
                break;
            case R.id.tv_about_net:
                IntentUtil.startBrower(mContext, getString(R.string.official_website));
                break;
            case R.id.tv_about_email:
                IntentUtil.sendEmail(mContext, getString(R.string.official_email));
                break;
        }
    }

    @Override
    public String getRequestTag() {
        return "AboutJinActivity";
    }

    @Override
    public void initialize() {
        tv_title_title.setText("关于国鑫");
        tv_preciousmetal_version.setText(ClientUtil.getVersionName(mContext));
    }
}
