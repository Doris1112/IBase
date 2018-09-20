package com.doris.ibase.utils;

import java.security.MessageDigest;

/**
 * MD5 加密解密
 *
 * @author Doris.
 * @date 2018/8/20.
 */
public class IMD5Utils {

    /**
     * 加密字符串
     *
     * @param info
     * @return
     */
    public static String getMD5Code(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes());
            byte[] encryption = md5.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    stringBuffer.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    stringBuffer.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            return "";
        }
    }

}
