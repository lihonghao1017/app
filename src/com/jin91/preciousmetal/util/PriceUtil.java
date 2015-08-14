package com.jin91.preciousmetal.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lijinhua on 2015/5/19
 * 行情的工具类.
 */
public class PriceUtil {

    private List<String> codeList;

    public PriceUtil() {
        codeList = new ArrayList<String>();
        codeList.add("XAGUSD");
        codeList.add("CU");
        codeList.add("AL");
        codeList.add("CU0");
        codeList.add("AL0");
        codeList.add("AG0");
        codeList.add("AGT+D");
        codeList.add("hf_NID");
    }
    /**
     * 得到播间的播主名字
     * @return
     */
    public static String getRoomTeacherName(String staffStatus)
    {
        if(!TextUtils.isEmpty(staffStatus))
        {
            String[] str = staffStatus.split(",");
            StringBuilder sb = new StringBuilder();
            for(String newStr:str)
            {
                String regex = "([\u4e00-\u9FFF]*.*?)";//为汉字
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(newStr);
                while(matcher.find())
                {
                    sb.append(matcher.group()).append(" ");
                }
            }
            return sb.toString().trim();
        }
        return  "";

    }
    /**
     * @Description:振幅
     * @return
     */

    public  String getSwing(double maxPrice, double minPrice, double open) {
        // {最高-最低}/昨收盘*100%
        double swing = (maxPrice - minPrice) / open * 100;
        return roundOff(2, swing) + "%";
    }
    public  String formatNum(double num, String code) {
        String num_str = num + "";
        if (code.indexOf('^') == 0 || code.indexOf('$') == 0) {
            // FOREX...
            num_str = roundOff(4, num);
        } else if (codeList.contains(code)) {
            num_str = roundOff(0, num);
        } else {
            num_str = roundOff(2, num);
        }
        return num_str;
    }

    /**
     * @param dig 保留的位数
     * @param d   原始数据
     * @return
     * @Description:四舍五入
     * @author: qiaoshl
     * @date:2014-5-19
     */

    public static String roundOff(int dig, double d) {
        return String.format("%." + dig + "f", d);
    }

    public static double getPersent(double sellPrice, double openPrice) {
        if (openPrice == Double.parseDouble("0")) {
            return Double.parseDouble("0");
        }
        return (sellPrice - openPrice) * 100 / openPrice;
    }
}
