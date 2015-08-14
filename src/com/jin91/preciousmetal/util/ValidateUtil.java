package com.jin91.preciousmetal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lijinhua on 2015/5/7.
 * 验证的工具类
 */
public class ValidateUtil {


    /**是否是手机号
     * @param str
     * @return
     */
    public static boolean isMobile(String str)
    {
        Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$"); // 验证手机号
        Matcher m = p.matcher(str);
        return  m.matches();

    }

    /**验证密码
     * @param str
     * @return
     */
    public static boolean checkPassword(String str)
    {
        Pattern p = Pattern.compile("^[@A-Za-z0-9!#$%^&*.~]{6,32}$");
        Matcher   m = p.matcher(str);
        return  m.matches();
    }
}
