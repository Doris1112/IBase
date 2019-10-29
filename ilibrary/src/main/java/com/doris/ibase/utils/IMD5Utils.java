package com.doris.ibase.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * MD5 加密
 *
 * @author Doris
 * @date 2018/8/20
 */
public class IMD5Utils {

    /**
     * 对传入的字符串进行MD5加密
     * @param info 需要加密的字符串
     * @return 返回加密后的字符串
     */
    public static String getMD5Code(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes());
            return convertToHexString(md5.digest());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 对传入的文件进行MD5加密
     * @param file 需要加密的文件
     * @return 返回加密后的字符串
     */
    public static String getMD5String(File file) {
        InputStream in = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int numRead;
            in = new FileInputStream(file);
            while ((numRead = in.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            return convertToHexString(md5.digest());
        } catch (Exception e) {
            return "";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String convertToHexString(byte[] encryption){
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : encryption) {
            if (Integer.toHexString(0xff & b).length() == 1) {
                stringBuilder.append("0").append(Integer.toHexString(0xff & b));
            } else {
                stringBuilder.append(Integer.toHexString(0xff & b));
            }
        }
        return stringBuilder.toString();
    }

}
