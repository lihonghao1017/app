package com.jin91.preciousmetal.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.IntentUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MsgNotifyActivity extends BaseActivity implements OnCheckedChangeListener{

	@ViewInject(R.id.tv_title_back)
	public TextView tv_title_back;
	@ViewInject(R.id.tv_title_option)
	public TextView tv_title_option;
	@ViewInject(R.id.tv_title_title)
	public TextView tv_title_title;
	@ViewInject(R.id.shengyin_tb)
	public ToggleButton shengyin_tb;
	@ViewInject(R.id.zhengdong_tb)
	public ToggleButton zhendong_tv;
	SharedPreferences sp;

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
		sp=this.getSharedPreferences("MsgSetting", Context.MODE_PRIVATE);
		shengyin_tb.setOnCheckedChangeListener(this);
		zhendong_tv.setOnCheckedChangeListener(this);
		zhendong_tv.setChecked(sp.getBoolean("zhendong", true));
		shengyin_tb.setChecked(sp.getBoolean("shengyin", true));
	}

	@OnClick({ R.id.tv_title_back})
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
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.shengyin_tb:
			sp.edit().putBoolean("shengyin", isChecked).commit();
			break;
		case R.id.zhengdong_tb:
			sp.edit().putBoolean("zhendong", isChecked).commit();
			break;
		default:
			break;
		}
	}
}