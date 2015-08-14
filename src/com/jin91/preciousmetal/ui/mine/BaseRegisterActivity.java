package com.jin91.preciousmetal.ui.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.InputUtil;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/7.
 */
public abstract class BaseRegisterActivity extends BaseActivity implements RegisterView {

    protected RegisterPresenter presenter;
    protected LoadingDialog loadingDialog;

    protected  int type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RegisterPresenterImpl(this);
    }


    @Override
    public void initialize() {
        presenter = new RegisterPresenterImpl(this);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
        loadingDialog.show();
    }

    @Override
    public void showNetError() {
        ((TextView)findViewById(R.id.tv_reg_msg)).setText(getString(R.string.net_error));
    }

    @Override
    public void hidLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showErrorMsg(String errMsg) {
    	 ((TextView)findViewById(R.id.tv_reg_msg)).setText(errMsg);
    }

    @Override
    public void hideSoftInput() {
        InputUtil.hideSoftInput(this);
    }

    @Override
    public void handleSuccess(String uid) {

    }

    @Override
    public void smsValidateSuccess() {

    }

    @Override
    public void checkPhoneSuccess() {

    }
}
