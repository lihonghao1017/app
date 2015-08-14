package com.jin91.preciousmetal.util;

import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import com.jin91.preciousmetal.R;

import java.lang.reflect.Field;

/**
 * Created by lijinhua on 2015/5/5.
 */
public class BuildApiUtil {

    /**
     * 通过反射设置edittext光标的颜色
     * @param editText
     */
    public static void setCursorDraw(EditText editText) {
        // 大于等于12
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            try {
                // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(editText, R.drawable.text_cursor_drawable);
            } catch (Exception ignored) {
            }
        }
    }
}
