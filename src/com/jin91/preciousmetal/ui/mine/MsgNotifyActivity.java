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

public class MsgNotifyActivity extends BaseActivity {

	@ViewInject(R.id.tv_title_back)
	public TextView tv_title_back;
	@ViewInject(R.id.tv_title_option)
	public TextView tv_title_option;
	@ViewInject(R.id.tv_title_title)
	public TextView tv_title_title;
	@ViewInject(R.id.shengyin_tb)
	public com.zcw.togglebutton.ToggleButton shengyin_tb;
	@ViewInject(R.id.zhengdong_tb)
	public com.zcw.togglebutton.ToggleButton zhendong_tv;

	public static void actionLaunch(Context context) {
		Intent intent = new Intent(context, MsgNotifyActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_notify_activity);
		ViewUtils.inject(this);
		initialize();
	}

	@OnClick({ R.id.tv_title_back, R.id.tv_about_callphone, R.id.tv_about_net,
			R.id.tv_about_email })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_back:
			finish();
			break;

		}
	}

	@Override
	public String getRequestTag() {
		return "MsgNotifyActivity";
	}

	@Override
	public void initialize() {
		tv_title_title.setText("消息提醒");
		shengyin_tb.setToggleOn();
	}
}