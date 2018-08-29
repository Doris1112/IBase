package com.doris.ibase.ilibrary.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Doris.
 * @date 2018/8/20.
 */
public class I3DESUtil {

    private final String Algorithm = "DESede";
    private String mPasswordCryptKey = "密钥";

    private static I3DESUtil instance;

    /**
     * 单例
     */
    public static I3DESUtil getInstance(String passwordCryptKey) {
        if (instance == null) {
            instance = new I3DESUtil(passwordCryptKey);
        } else if (!instance.mPasswordCryptKey.equals(passwordCryptKey)){
            instance = new I3DESUtil(passwordCryptKey);
        }
        return instance;
    }

    private I3DESUtil(String passwordCryptKey) {
        mPasswordCryptKey = passwordCryptKey;
    }

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public String encryptMode(String str) throws Exception {
        SecretKey desKey = new SecretKeySpec(build3DesKey(mPasswordCryptKey), Algorithm);
        Cipher cipher = Cipher.getInstance(Algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, desKey);
        return new String(cipher.doFinal(str.getBytes()));
    }

    /**
     * 解密
     *
     * @param str
     * @return
     */
    public String decryptMode(String str) throws Exception {
        SecretKey desKey = new SecretKeySpec(build3DesKey(mPasswordCryptKey), Algorithm);
        Cipher cipher = Cipher.getInstance(Algorithm);
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        return new String(cipher.doFinal(str.getBytes()));
    }

    /**
     * 生成密钥
     *
     * @param keyStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes();
        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
