package com.human.tools.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * <>MD5验签</>
 *
 * @author shaonan.hu
 * @version V1.0
 * @Time 2019/4/9
 */
public class MD5Util {

    private static MessageDigest md;

    /**
     * 签名字符串
     *
     * @param text    需要签名的字符串
     * @param charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String charset) throws Exception {
        return DigestUtils.md5Hex(getContentBytes(text, charset));
    }

    /**
     * 签名字符串
     *
     * @param text    需要签名的字符串
     * @param sign    签名结果
     * @param charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String charset) throws Exception {
        String mysign = DigestUtils.md5Hex(getContentBytes(text, charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 通联签名
     *
     * @param b
     * @return
     */
    public static String computeDigest(byte[] b) {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new String(b);
        }
        md.reset();
        md.update(b);
        byte[] hash = md.digest();
        StringBuffer outStrBuf = new StringBuffer(32);
        for (int i = 0; i < hash.length; i++) {
            int v = hash[i] & 0xFF;
            if (v < 16) {
                outStrBuf.append('0');
            }
            outStrBuf.append(Integer.toString(v, 16).toLowerCase());
        }
        return outStrBuf.toString();
    }

    public static void main(String[] args) {
        try {
            JSONObject map = new JSONObject();
            map.put("msg", "xxx");
            map.put("code", "200");
            map.put("data", "hhhh");

            String strSign = sign(SortUtil.formatParamMap(map.toJSONString()), "UTF-8");
            System.out.println("加签结果：" + strSign);
            System.out.println(verify(SortUtil.formatParamMap(map.toJSONString()), strSign, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
