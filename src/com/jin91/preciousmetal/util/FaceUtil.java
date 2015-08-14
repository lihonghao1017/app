package com.jin91.preciousmetal.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.jin91.preciousmetal.R;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lijinhua on 2015/5/11.
 * 表情的工具类
 */
public class FaceUtil {

    public static String regular = "\\[EMOT\\][0-9]{1}\\[/EMOT\\]|" + // 0-9
            "\\[EMOT\\][1-9]{1}[0-9]{1}\\[/EMOT\\]|" + // 10-99
            "\\[EMOT\\][1]{1}[0-4]{1}[0-9]{1}\\[/EMOT\\]|" + // 100-140
            "\\[EMOT\\][1]{1}[4]{1}[1-8]{1}\\[/EMOT\\]";// 141-147

//    public static SpannableStringBuilder textAddFace(Context mContext, String content) {
//        if (TextUtils.isEmpty(content)) {
//            content = "";
//            SpannableStringBuilder ss = new SpannableStringBuilder(content);
//            return ss;
//        }
//        SpannableStringBuilder ss = new SpannableStringBuilder(Html.fromHtml(content));
//        Pattern pattern = Pattern.compile(regular);
//        Matcher matcher = pattern.matcher(content);
//        while (matcher.find()) {
//            String key = matcher.group();
//            String face = key.replace("[EMOT]", "").replace("[/EMOT]", "");
//            try {
//                Field field = R.drawable.class.getDeclaredField("face" + face);
//                int resId = Integer.parseInt(field.get(null).toString()); // 通过上面匹配得到的字符串来生成图片资源id
//                if (resId != 0) {
//                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
//                    ImageSpan imageSpan = new ImageSpan(bitmap); // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
//                    int qstart = matcher.start();
//                    int end = qstart + key.length(); // 计算该图片名字的长度，也就是要替换的字符串的长度
//                    ss.setSpan(imageSpan, qstart, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return ss;
//    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
        Matcher matcher = patten.matcher(spannableString);
        try {
            while (matcher.find()) {
                String key = matcher.group();
                if (matcher.start() < start) {
                    continue;
                }
                String face = key.replace("[EMOT]", "").replace("[/EMOT]", "");
                String kes = "face" + face;
                Field field = R.drawable.class.getDeclaredField(kes);
                int resId = Integer.parseInt(field.get(null).toString()); // 通过上面匹配得到的字符串来生成图片资源id
                if (resId != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                    ImageSpan imageSpan = new ImageSpan(bitmap); // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                    int qstart = matcher.start();
                    int end = qstart + key.length(); // 计算该图片名字的长度，也就是要替换的字符串的长度
                    spannableString.setSpan(imageSpan, qstart, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
                    if (end < spannableString.length()) { // 如果整个字符串还未验证完，则继续。。
                        dealExpression(context, spannableString, patten, end);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public static SpannableString textAddFace(Context context, String str) {
        SpannableString spannableString = new SpannableString(Html.fromHtml(str));
        Pattern sinaPatten = Pattern.compile(regular, Pattern.CASE_INSENSITIVE); // 通过传入的正则表达式来生成一个pattern
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("qsl", e.getMessage());
        }
        return spannableString;
    }
}
