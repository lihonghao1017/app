package com.jin91.preciousmetal.ui.price;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SMASettingActivity extends Activity {
	@ViewInject(R.id.tv_title_back)
	public TextView tv_title_back;
	@ViewInject(R.id.tv_title_option)
	public TextView tv_title_option;
	@ViewInject(R.id.tv_title_title)
	public TextView tv_title_title;
	@ViewInject(R.id.SMASettingActivity_radio01)
	public RadioButton radio01;
	@ViewInject(R.id.SMASettingActivity_radio02)
	public RadioButton radio02;
	@ViewInject(R.id.SMASettingActivity_radio03)
	public RadioButton radio03;
	@ViewInject(R.id.SMASettingActivity_radio04)
	public RadioButton radio04;
	@ViewInject(R.id.SMASettingActivity_radio05)
	public RadioButton radio05;
	@ViewInject(R.id.SMASettingActivity_spinner)
	public Spinner spinner;
	@ViewInject(R.id.SMASettingActivity_middle01)
	private EditText et_01;
	@ViewInject(R.id.SMASettingActivity_middle02)
	private EditText et_02;
	@ViewInject(R.id.SMASettingActivity_middle03)
	private EditText et_03;
	@ViewInject(R.id.SMASettingActivity_middle04)
	private EditText et_04;
	@ViewInject(R.id.SMASettingActivity_add01)
	private ImageView left01;
	@ViewInject(R.id.SMASettingActivity_add02)
	private ImageView left02;
	@ViewInject(R.id.SMASettingActivity_add03)
	private ImageView left03;
	@ViewInject(R.id.SMASettingActivity_add04)
	private ImageView left04;
	@ViewInject(R.id.SMASettingActivity_reduce01)
	private ImageView right01;
	@ViewInject(R.id.SMASettingActivity_reduce02)
	private ImageView right02;
	@ViewInject(R.id.SMASettingActivity_reduce03)
	private ImageView right03;
	@ViewInject(R.id.SMASettingActivity_reduce04)
	private ImageView right04;
	private static final String[] m = { "算数平均", "线性加权", "指数加权", "三角" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sma_setng_actvity_layout);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		tv_title_title.setText("SMA均匀线");
		tv_title_option.setText("保存");
		spinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, m));
		spinner.setSelection(1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				((TextView) view).setText(m[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@OnClick({ R.id.tv_title_back, R.id.tv_title_option,
			R.id.SMASettingActivity_reduce01, R.id.SMASettingActivity_reduce02,
			R.id.SMASettingActivity_reduce03, R.id.SMASettingActivity_reduce04,
			R.id.SMASettingActivity_add01, R.id.SMASettingActivity_add02,
			R.id.SMASettingActivity_add03, R.id.SMASettingActivity_add04,
			R.id.tv_cycle, R.id.SMASettingActivity_radio01,
			R.id.SMASettingActivity_radio02, R.id.SMASettingActivity_radio03,
			R.id.SMASettingActivity_radio04, R.id.SMASettingActivity_radio05 })
	public void setOnClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_back:
			finish();
			break;
		case R.id.tv_title_option:

			break;
		case R.id.SMASettingActivity_radio01:
			setRadioButtton(1);
			break;
		case R.id.SMASettingActivity_radio02:
			setRadioButtton(2);
			break;
		case R.id.SMASettingActivity_radio03:
			setRadioButtton(3);
			break;
		case R.id.SMASettingActivity_radio04:
			setRadioButtton(4);
			break;
		case R.id.SMASettingActivity_radio05:
			setRadioButtton(5);
			break;
		case R.id.SMASettingActivity_reduce01:
			et_01.setText((Integer.parseInt(et_01.getText().toString()) + 1)+"");
			break;
		case R.id.SMASettingActivity_reduce02:
			et_02.setText((Integer.parseInt(et_02.getText().toString()) + 1)+"");
			break;
		case R.id.SMASettingActivity_reduce03:
			et_03.setText((Integer.parseInt(et_03.getText().toString()) + 1)+"");
			break;
		case R.id.SMASettingActivity_reduce04:
			et_04.setText((Integer.parseInt(et_04.getText().toString()) + 1)+"");
			break;
		case R.id.SMASettingActivity_add01:
			et_01.setText((Integer.parseInt(et_01.getText().toString()) - 1)+"");
			break;
		case R.id.SMASettingActivity_add02:
			et_02.setText((Integer.parseInt(et_02.getText().toString()) - 1)+"");
			break;
		case R.id.SMASettingActivity_add03:
			et_03.setText((Integer.parseInt(et_03.getText().toString()) - 1)+"");
			break;
		case R.id.SMASettingActivity_add04:
			et_04.setText((Integer.parseInt(et_04.getText().toString()) - 1)+"");
			break;
		}
	}

	private void setRadioButtton(int pos) {
		radio01.setChecked(pos == 1 ? true : false);
		radio02.setChecked(pos == 2 ? true : false);
		radio03.setChecked(pos == 3 ? true : false);
		radio04.setChecked(pos == 4 ? true : false);
		radio05.setChecked(pos == 5 ? true : false);
	}
}
