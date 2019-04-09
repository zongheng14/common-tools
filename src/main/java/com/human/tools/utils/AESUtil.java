package com.human.tools.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * <>AES加解密算法</>
 *
 * @author shaonan.hu
 * @version V1.0
 * @Time 2019/4/9
 */
public class AESUtil {

    static final String MODE = "AES/ECB/PKCS5Padding";

    /**
     * AES加密
     *
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String content, String key) throws Exception {
        if (!checkString(content) || !checkString(key)) {
            return null;
        }
        content = Base64.getEncoder().encodeToString(content.getBytes("UTF-8"));
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key), "AES"));
        byte[] bytes = cipher.doFinal(content.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 检查字符串
     *
     * @param text
     * @return
     */
    public static boolean checkString(String text) {
        return text != null && !"".equalsIgnoreCase(text);
    }

    /**
     * AES解密
     *
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String content, String key) throws Exception {
        if (content == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key), "AES"));
        byte[] bytes = Base64.getDecoder().decode(content);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, "UTF-8");
    }

    /**
     * md5加密
     *
     * @param appId       应用id
     * @param queryParams 参数
     * @param content     请求body
     * @return
     */
    public static String md5(String appId, String queryParams, String content) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            if (queryParams == null) {
                queryParams = "";
            }
            if (content == null) {
                content = "";
            }
            String contentText = appId + queryParams + content;
            byte[] bytes = md5.digest(contentText.getBytes("UTF-8"));
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
