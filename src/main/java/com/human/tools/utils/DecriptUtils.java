package com.human.tools.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DecriptUtils {
/*    // 定义加密算法，有DES、DESede(即3DES)、Blowfish
    private static final String ALGORITHM = "DESede";
    // 定义加密算法:
    // 有DES->DES/ECB/PKCS5Padding、
    // DESede(即3DES)->DESede、Blowfish
    private static final String CIPHER_ALGORITHM = "DESede";*/


    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 加密
     *
     * @param key
     * @param value
     * @return
     */
    public static String encryptECB(String key, String value) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        SecretKey secretKey = new SecretKeySpec(build3DesKey(key), "DESede");
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] binaryData = cipher.doFinal(value.getBytes("UTF-8"));
        return Base64.encodeBase64String(binaryData);
    }

    /**
     * 解密
     *
     * @param key
     * @param value
     * @return
     */
    public static String decryptECB(String key, String value) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        byte[] binaryValue = Base64.decodeBase64(value);
        SecretKey secretKey = new SecretKeySpec(build3DesKey(key), "DESede");
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] data = cipher.doFinal(binaryValue);
        return new String(data, "UTF-8");
    }

    /*
	 * 根据字符串生成密钥字节数组
	 * @param keyStr 密钥字符串
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8"); // 将字符串转成字节数组
		/*
		 * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
		 */
        if (key.length > temp.length) {
            // 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            // 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    public static void main(String[] args) throws Exception {

        String key = "personal_service_provider_key_01";
        key = MD5Util2.getMD5String(key);
        System.out.println("key-->"+key);

        String keys[]={
                "xxx_medical_client_xxx",
                "xxxclient_medical_xxx",
                "{\"productCode\":\"312313\",\"channelId\":\"312\"}",
                "xxx_client_contract_xxx",
                "xxx_contract_client_xxx",
                "xxx_settlement_client_xxx",
                "xxx_settlement_client_xxx",
                "xxx_adjustment_client_xxx"
        };

        String password_crypt_key = "www";
//        for(int i=0;i<keys.length;i++){
//            password_crypt_key=MD5Util.getMD5String(keys[i]+"01312018_dev");
//            System.out.println(keys[i]+" => "+password_crypt_key);
//        }


        /**
         * 加密
         */
        String str=encryptECB(key,keys[2]);
        System.out.println(str);
        /**
         * 解密
         */
        System.out.println(decryptECB(key,str));
    }



}
