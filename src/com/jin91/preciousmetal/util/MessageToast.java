package com.jin91.preciousmetal.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.jin91.preciousmetal.ui.PreciousMetalAplication;

public class MessageToast {

    private static ToastRunnable sToastRunnable = new ToastRunnable();
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static long sLastTime;
    private static final long DUR = 2000L;
    private static Toast sToast;

    private static class ToastRunnable implements Runnable {
        String msg;
        int msgId;
        int time;

        @Override
        public void run() {
            Context context = PreciousMetalAplication.getContext();
            sLastTime = System.currentTimeMillis();
            if (msg != null) {
                sToast = Toast.makeText(context, msg, time);
            } else {
                if (msgId == 0) {
                    return;
                }
                sToast = Toast.makeText(context, msgId, time);
            }
            sToast.show();
        }
    }

    public static void showToast(String message, int time) {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
        sHandler.removeCallbacks(sToastRunnable);

        sToastRunnable.msg = message;
        sToastRunnable.msgId = 0;
        sToastRunnable.time = time;

        long dur = System.currentTimeMillis() - sLastTime;
        if (dur < DUR) {
            sHandler.postDelayed(sToastRunnable, DUR - dur);
        } else {
            sHandler.postDelayed(sToastRunnable, 0);
        }
    }

    public static void showToast(int messageResId, int time) {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
        sHandler.removeCallbacks(sToastRunnable);

        sToastRunnable.msg = null;
        sToastRunnable.msgId = messageResId;
        sToastRunnable.time = time;

        long dur = System.currentTimeMillis() - sLastTime;
        if (dur < DUR) {
            sHandler.postDelayed(sToastRunnable, DUR - dur);
        } else {
            sHandler.postDelayed(sToastRunnable, 0);
        }
    }

}