package com.jin91.preciousmetal.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

/**
 * Created by lijinhua on 2015/4/29.
 * 图片的工具类
 */
public class ImageUtil {

    public static final String MOBLIE_TAG = "M_";

    /**
     * 得到手机的真真图片地址
     * @param link
     * @return
     */
    public static String ChangeImgUrlToMoblie(String link)// 改变图片地址为mobile专用
    {
        String result_name = null;
        // String url="http://192.168.0.25:833/uploads/648111a7-069f-4988-afd4-16aa86857d92.gif";
        int idx = link.lastIndexOf("/", link.length());
        String firsturl = link.substring(0, idx + 1);
        String name = link.substring(idx + 1, link.length());
        if (!name.startsWith(MOBLIE_TAG)) {
            result_name = MOBLIE_TAG + name;/*
											 * int idxbegin=url.indexOf("//",0); String
											 * subString=url.substring(idxbegin);
											 */

            // subString.substring();
            // LogUtil.i("change", firsturl+result_name);
        }
        return firsturl + result_name;
    }
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public static  byte[] bitmapresize(Bitmap image, int size) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 85, out);
        float zoom = (float) Math.sqrt((size * 1024) / (float) out.toByteArray().length);
        //
        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);
        //
        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        //
        out.reset();
        result.compress(Bitmap.CompressFormat.JPEG, 85, out);
//        Logger.i(TAG, "bitmapresize=whiel之前==" + out.toByteArray().length / 1024);
        while (out.toByteArray().length > size * 1024) {
//            Logger.i(TAG, "bitmapresize=whiel  中==" + out.toByteArray().length / 1024);
            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            out.reset();
            result.compress(Bitmap.CompressFormat.JPEG, 85, out);
        }
        return out.toByteArray();
    }
}
