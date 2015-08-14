package com.jin91.preciousmetal.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.IntentUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/5.
 * 使用协议
 */
public class UserProtocolActivity extends BaseActivity {


    public static void actionLaunch(Context context) {
        Intent intent = new Intent(context, UserProtocolActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_user_protcol);
        ViewUtils.inject(this);
    }

    @Override
    public String getRequestTag() {
        return "UserProtocolActivity";
    }

    @OnClick({R.id.tv_title_back, R.id.tv_protcol_phone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.tv_protcol_phone:
                IntentUtil.callPhone(mContext, getString(R.string.official_tel));
                break;
        }
    }

    @Override
    public void initialize() {

    }
}
