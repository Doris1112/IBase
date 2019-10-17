package com.doris.ibase.utils;

import java.security.MessageDigest;

/**
 * MD5 加密解密
 *
 * @author Doris
 * @date 2018/8/20
 */
public class IMD5Utils {

    /**
     * 加密字符串
     */
    public static String getMD5Code(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes());
            byte[] encryption = md5.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : encryption) {
                if (Integer.toHexString(0xff & b).length() == 1) {
                    stringBuilder.append("0").append(Integer.toHexString(0xff & b));
                } else {
                    stringBuilder.append(Integer.toHexString(0xff & b));
                }
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }

}
