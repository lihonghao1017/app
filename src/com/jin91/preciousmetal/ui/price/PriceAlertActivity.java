package com.jin91.preciousmetal.ui.price;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Alert;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.ui.mine.LoginActivity;
import com.jin91.preciousmetal.util.BuildApiUtil;
import com.jin91.preciousmetal.util.InputUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/4/30.
 * 价格提醒
 */
public class PriceAlertActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, PriceAlertView ,TextWatcher{

    public static final String TAG = "PriceAlertActivity";
    public static final int REQUEST_SELECT_PRICE_CODE = 101; // 选择行情的请求码

    @ViewInject(R.id.tr_trade_name)
    TextView tr_trade_name;
    @ViewInject(R.id.tv_trade_pre)
    TextView tv_trade_pre;
    @ViewInject(R.id.tv_trade_sellprice)
    TextView tv_trade_sellprice;
    @ViewInject(R.id.rg_alert_type)
    RadioGroup rg_alert_type;
    @ViewInject(R.id.rg_oper_direction)
    RadioGroup rg_oper_direction;
    @ViewInject(R.id.et_price)
    EditText et_price;
    @ViewInject(R.id.rg_valid_time_2448)
    RadioGroup rg_valid_time_2448;
    @ViewInject(R.id.rg_valid_time_7296)
    RadioGroup rg_valid_time_7296;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    private String tradeCode;// 交易的品种
    private int way = 1;// 方式(本地提醒--1,短信提醒--2)
    private int direction = 0; // 小于等于---0,大于等于 1
    private int duration = 24; // 24  48  72 96
    private PriceAlertPresenter presenter;
    private LoadingDialog loadingDialog;

    /**
     * @param context
     * @param tradeCode   交易码
     * @param titleName   交易的名称
     * @param sellPrice   卖价
     * @param increase    增长
     * @param way         提醒的方式 (本地提醒--1,短信提醒--2)
     * @param requestCode 300是从报价提醒页面过来的
     */
    public static void actionLaunch(Context context, String tradeCode, String titleName, String sellPrice, String increase, int way, int requestCode) {
        Intent i = new Intent(context, PriceAlertActivity.class);
        i.putExtra("tradeCode", tradeCode);
        i.putExtra("titleName", titleName);
        i.putExtra("sellPrice", sellPrice);
        i.putExtra("increase", increase);
        i.putExtra("way", way);
        ((Activity) context).startActivityForResult(i, requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_alert);
        ViewUtils.inject(this);
        initialize();
    }

    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void initialize() {
        tv_title_option.setText(getString(R.string.save));
        tv_title_title.setText(getString(R.string.new_alert));
        Intent intent = getIntent();
        tradeCode = intent.getStringExtra("tradeCode");
        tr_trade_name.setText(intent.getStringExtra("titleName"));
        tv_trade_sellprice.setText(intent.getStringExtra("sellPrice"));
        tv_trade_pre.setText(intent.getStringExtra("increase"));
        way = intent.getIntExtra("way", 1);
        if (way == 2) {
            rg_alert_type.check(R.id.rb_msg_alert);
        }
        BuildApiUtil.setCursorDraw(et_price);
        et_price.addTextChangedListener(this);
        loadingDialog = new LoadingDialog(mContext, false);
        presenter = new PriceAlertPresenterImpl(this);
        rg_alert_type.setOnCheckedChangeListener(this); // 交易方式 本地提醒 短信提醒
        rg_oper_direction.setOnCheckedChangeListener(this);// 交易方向 小于等于，大于等于
        rg_valid_time_2448.setOnCheckedChangeListener(this);// 时效 24 48 72  96
        rg_valid_time_7296.setOnCheckedChangeListener(this);// 时效 24 48 72  96
    }

    @OnClick({R.id.tv_title_back, R.id.tv_title_option, R.id.rl_select_trade})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.tv_title_option:
                saveAlert();
                break;
            case R.id.rl_select_trade:
                InputUtil.hideSoftInput(mContext, et_price.getWindowToken());
                SelectPriceActivity.actionLaunch(mContext, REQUEST_SELECT_PRICE_CODE);
                break;
        }
    }

    /**
     * 保存提醒
     */
    private void saveAlert() {
        if (PreciousMetalAplication.getINSTANCE().user == null) {
            LoginActivity.actionLaunch(mContext);
            return;
        }

        if (way == 1) {
            // 本地提醒
            Alert alert = new Alert();
            alert.direction = direction;
            alert.way = way;
            alert.tradeCode = tradeCode;
            alert.phoneNum = "0";
            alert.price = et_price.getText().toString().trim();
            alert.duration = duration;
            presenter.saveAlert(TAG, alert);
        } else {
            // 短信提醒
            String price = et_price.getText().toString().trim();
            if (TextUtils.isEmpty(tradeCode)) {
                showToastMessage("请选择要提醒的行情");
                return;
            }
            if (TextUtils.isEmpty(price)) {
                showToastMessage("请输入价格");
                return;
            }

            presenter.getMobile(TAG);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_local_alert:
                way = 1;
                InputUtil.hideSoftInput(mContext, et_price.getWindowToken());
                break;
            case R.id.rb_msg_alert:
                way = 2;
                InputUtil.hideSoftInput(mContext, et_price.getWindowToken());
                break;
            case R.id.rb_lt_equal:
                direction = 0;
                InputUtil.hideSoftInput(mContext, et_price.getWindowToken());
                break;
            case R.id.rb_gt_equal:
                direction = 1;
                InputUtil.hideSoftInput(mContext, et_price.getWindowToken());
                break;
            case R.id.rb_valid_24:
                duration = 24;
                break;
            case R.id.rb_valid_48:
                duration = 48;
                break;
            case R.id.rb_valid_72:
                duration = 72;
                break;
            case R.id.rb_valid_96:
                duration = 96;
                break;
        }
    }


    @Override
    public void showProcessDialog() {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void hideProcessDialog() {
        loadingDialog.dismiss();
    }

    @Override
    public void showToastNet() {
        MessageToast.showToast(R.string.net_error, 0);
    }

    @Override
    public void showToastMessage(String message) {
        MessageToast.showToast(message, 0);
    }

    @Override
    public void saveAlertSuccess() {
        MessageToast.showToast("提醒设置成功", 0);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setPhone(String phone) {
        Alert alert = new Alert();
        alert.direction = direction;
        alert.way = way;
        alert.tradeCode = tradeCode;
        alert.phoneNum = phone;
        alert.price = et_price.getText().toString().trim();
        alert.duration = duration;
        presenter.saveAlert(TAG, alert);
    }

    @Override
    public void hideInput() {
        InputUtil.hideSoftInput(mContext, et_price.getWindowToken());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK && requestCode == REQUEST_SELECT_PRICE_CODE) {
            tradeCode = data.getStringExtra("tradeCode");
            tr_trade_name.setText(data.getStringExtra("titleName"));
            tv_trade_sellprice.setText(data.getStringExtra("sellPrice"));
            tv_trade_pre.setText(data.getStringExtra("increase"));
        }
    }
    private boolean isChanged = false;      
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		  if (isChanged) {// ----->如果字符未改变则返回      
	            return;      
	        }      
	        String str = s.toString();      
	  
	        isChanged = true;      
	        String cuttedStr = str;    
	        boolean flag=false;  
	        /* 删除字符串中的dot */      
	        for (int i = str.length() - 1; i >= 0; i--) {      
	            char c = str.charAt(i);      
	            if ('.' == c&&i==str.length() - 6) {  
	                cuttedStr = str.substring(0, i+5);   
	                if(cuttedStr.endsWith(".")){  
	                    cuttedStr=cuttedStr.substring(0, i+5);  
	                }  
	                flag=true; 
	                break;  
	            }      
	        }      
	        if(flag){  
	        	et_price.setText(cuttedStr);      
	        }  
//	        edit.setSelection(edit.length());      
	        isChanged = false;      
	}
}
