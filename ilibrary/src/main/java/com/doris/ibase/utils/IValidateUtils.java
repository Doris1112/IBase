package com.doris.ibase.utils;

import android.text.TextUtils;

/**
 * Created by Doris on 2018/9/3.
 */
public class IValidateUtils {

    /**
     * 判断手机号码是否正确
     *
     * @return 是否
     */
    public static boolean checkMobile(String mobile) {
        if (!mobile.matches("^1[3|4|5|6|7|8|9][0-9]\\d{8}$")) {
            return false;
        }
        return true;
    }

    /**
     * 判断邮箱是否正确
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        if (!email.matches("[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z]{2,5}")) {
            return false;
        }
        return true;
    }

    /**
     * 检查字符在某个区间
     * @param text
     * @param min
     * @param max
     * @return
     */
    public static boolean checkLengthInterval(String text, int min, int max){
        if (TextUtils.isEmpty(text) || text.length() < min || text.length() > max) {
            return false;
        }
        return true;
    }
}
