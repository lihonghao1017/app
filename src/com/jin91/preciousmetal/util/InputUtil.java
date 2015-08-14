package com.jin91.preciousmetal.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lijinhua on 2015/5/6.
 */
public class InputUtil {

    //输入法是否显示着
    public static boolean isShowKeyBoard(EditText edittext) {
        boolean bool = false;
        InputMethodManager imm = (InputMethodManager) edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            bool = true;
        }
        return bool;

    }

    //强制显示或者关闭系统键盘
    public static void KeyBoard(final EditText txtSearchKey, final String status) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (status.equals("open")) {
                    m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
                } else {
                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
                }
            }
        }, 300);
    }

    //通过定时器强制隐藏虚拟键盘
    public static void TimerHideKeyboard(final View v) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
            }
        }, 10);
    }

    /**
     * 传递editText打开键盘
     *
     * @param context
     * @param editText
     */
    public static void openKeyboard(final Context context, final EditText editText) {
        editText.requestFocus();
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, InputMethodManager.SHOW_FORCED);

//            }
//        }, 200);
    }

    /**
     * 隐藏输入法
     *
     * @param context
     * @param binder
     */
    public static void hideSoftInput(Context context, IBinder binder) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binder, 0);

    }

    /**
     * @param context
     * @return
     * @Description:软键盘显示时被成功隐藏 返回true
     */
    public static boolean hideSoftInput(Activity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && context.getCurrentFocus() != null && context.getCurrentFocus().getWindowToken() != null) {
                return inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        }
        return false;
    }
}
