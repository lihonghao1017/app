package com.jin91.preciousmetal.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.util.ScreenUtil;

/**
 * Created by lijinhua on 2015/5/21.
 * 两个按钮的dialog
 */
public class CustomDialog extends Dialog implements View.OnClickListener {

    private CustomDialogListener listener;
    private TextView tvDialogTitle;
    private TextView tvDialogContent;
    private TextView tvOk;
    private TextView tvCancel;

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context mContext, CustomDialogListener listener, boolean outside, boolean canable) {
        super(mContext, R.style.CustomDialogTheme);
        this.listener = listener;
        setCancelable(canable);
        setCanceledOnTouchOutside(outside);
        initView();

        int width = getWindow().getWindowManager().getDefaultDisplay().getWidth();

        getWindow().setLayout(width - ScreenUtil.dip2px(mContext, 50), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog, null);
        tvDialogTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvDialogContent = (TextView) view.findViewById(R.id.tv_dialog_content);
        tvCancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
        tvCancel.setOnClickListener(this);
        tvOk = (TextView) view.findViewById(R.id.tv_dialog_ok);
        tvOk.setOnClickListener(this);
        setContentView(view);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
    }

    public void setTvDialogTitle(String title) {
        tvDialogTitle.setText(title);
    }

    public void setTvDialogContent(String content) {
        tvDialogContent.setText(content);
    }

    public void setTvOkText(String title) {
        tvOk.setText(title);
    }

    public void setHideTvCancel() {
        tvCancel.setVisibility(View.GONE);
    }

    public void setHideContent() {
        tvDialogContent.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dialog_cancel:
                dismiss();
                if (listener != null) {
                    listener.customButtonListener(1);
                }
                break;
            case R.id.tv_dialog_ok:
                if (listener != null) {
                    dismiss();
                    listener.customButtonListener(2);
                }

                break;
        }
    }

    public interface CustomDialogListener {
        /**
         * 0 表示取消按钮
         * 1 表示确定按钮
         *
         * @param postion
         */
        void customButtonListener(int postion);

    }
}
