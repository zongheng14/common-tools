package com.human.tools.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 Util
 * <p/>
 * MD5工具类
 */

public class MD5Util2 {

    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5Util message digest initialize error" + e.getMessage());
        }
    }

    public static String getMD5(InputStream is) throws NoSuchAlgorithmException, IOException {
        StringBuffer md5 = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = is.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }
        ;
        byte[] mdbytes = md.digest();

        // convert the byte to hex format
        for (int i = 0; i < mdbytes.length; i++) {
            md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return md5.toString();
    }


    public static synchronized String getFileMD5String1(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    public static synchronized String getFileMD5String(File file) throws IOException {
        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024 * 16];
        int numRead;
        while ((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(messagedigest.digest());
    }

   /* public static synchronized String getSha1MD5String(String s, String random) {
        String str = DigestUtils.shaHex(random);
        return getMD5String((str + s).getBytes());
    }*/


    public static synchronized String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static synchronized String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }


/*    public static void main(String[] args) throws Exception {
        String date = "{\"messageHead\":{\"sendTime\":\"2016-08-15 22:46:43\",\"action\":\"Q1\",\"sourceId\":\"61131\"},\"callback\":{\"order\":{\"productCode\":\"252\",\"policyUrl\":\"http://mall.taikang.com/webapp/downFilepolicy?policyNo=21162520879547382&source=61131&md5s=76b2c8e5c21fcd87733607484a490cda\",\"endDate\":\"2022-08-16 00:00:00\",\"policyNo\":\"21162520879547382\",\"beginDate\":\"2016-08-16 00:00:00\",\"totalPremium\":\"1000\",\"clientNo\":\"88\"},\"clientInfo\":{\"insuredInfo\":{\"insuredList\":[null],\"isHolder\":\"true\",\"benefitInfo\":{\"isLegal\":\"true\",\"benefitList\":[null]}},\"holder\":{\"holderCardNo\":\"O3A3KUmWxaqGunZ6vYiTBpwUuZnNua3/\",\"holderSex\":\"0\",\"holderBirthday\":\"1979-05-30\",\"holderMobile\":\"aMGsSUeWULofXqVMn/NDPd+ajEhDyoxI1sncEHMW/Hx8zOxuvTM4qg==\",\"holderCardType\":\"01\",\"holderName\":\"\\\\u81e7\\\\u65ed\"}}}}";
        System.out.println(date);
//        System.out.println(unicode2String("\\u65ed"));
    }*/

}