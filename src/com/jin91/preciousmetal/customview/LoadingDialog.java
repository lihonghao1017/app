package com.jin91.preciousmetal.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.jin91.preciousmetal.R;

/**
 * Created by lijinhua on 2015/5/5.
 * 加载的dialog
 */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, boolean outsideCancel) {
        super(context, R.style.CustomDialogTheme);
        setCanceledOnTouchOutside(outsideCancel);
        initView();
    }

    public void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.loading_dialog, null);
        setContentView(view);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER); //此处可以设置dialog显示的位置
//        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }
}
