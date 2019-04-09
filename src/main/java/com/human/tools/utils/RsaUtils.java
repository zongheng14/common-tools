package com.human.tools.utils;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密解实现。
 *
 * @author shaonan.hu
 * @version V1.0
 * @Time 2019/4/9
 */
public class RsaUtils {

    /**
     * 算法名称
     */
    private final static String RSA = "RSA";

    /**
     * 加密后的字节分隔长度
     */
    private final static int encryptSepLength = 256;

    /**
     * 明文字节分隔长度
     */
    private final static int plainSepLneght = 100;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static byte[] encrypt(byte[] text, PublicKey pubRSA) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, pubRSA);
        return cipher.doFinal(text);
    }

    /**
     * 新的加密方式
     * 旧的其他系统再用，勿做更改
     *
     * @param text
     * @param uk
     * @param isTrue：只是用于区别于旧解密方法，此字段无用
     * @return
     * @throws Exception
     */
    private final static String encrypt(String text, PublicKey uk, Boolean isTrue) {

        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, uk);
            int inputLen = text.getBytes().length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(text.getBytes(), offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(text.getBytes(), offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
            // 加密后的字符串
            return new String(Base64.encodeBase64String(encryptedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    private final static String encrypt(String text, PublicKey uk) {
        StringBuffer sbf = new StringBuffer(200);
        try {
            text = URLEncoder.encode(text, "UTF-8");
            byte[] plainByte = text.getBytes();
            ByteArrayInputStream bays = new ByteArrayInputStream(plainByte);
            byte[] readByte = new byte[plainSepLneght];
            int n = 0;
            while ((n = bays.read(readByte)) > 0) {
                if (n >= plainSepLneght) {
                    sbf.append(byte2hex(encrypt(readByte, uk)));
                } else {
                    byte[] tt = new byte[n];
                    for (int i = 0; i < n; i++) {
                        tt[i] = readByte[i];
                    }
                    sbf.append(byte2hex(encrypt(tt, uk)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sbf.toString();
    }

    /**
     * 新的解密方式
     * 旧的其他系统再用，勿做更改
     *
     * @param data
     * @param rk
     * @param isTrue：只是用于区别于旧解密方法，此字段无用
     * @return
     * @throws Exception
     */
    private final static String decrypt(String data, PrivateKey rk, Boolean isTrue) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, rk);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, 128);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    @Deprecated
    private final static String decrypt(String data, PrivateKey rk) throws Exception {
        String rrr = "";
        StringBuffer sb = new StringBuffer(100);
        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
        byte[] readByte = new byte[256];
        int n = 0;
        while ((n = bais.read(readByte)) > 0) {
            if (n >= encryptSepLength) {
                sb.append(new String(decrypt(hex2byte(readByte), rk)));
            } else {

            }
        }
        rrr = URLDecoder.decode(sb.toString(), "UTF-8");
        return rrr;
    }

    private static byte[] decrypt(byte[] src, PrivateKey rk) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, rk);
        return cipher.doFinal(src);
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs += ("0" + stmp);
            else
                hs += stmp;
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");

        byte[] b2 = new byte[b.length / 2];

        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static PrivateKey getPrivateKey(String privateKeyStr) {
        try {
            byte[] keyBytes = new BASE64Decoder().decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey prk = keyFactory.generatePrivate(keySpec);
            return prk;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PublicKey getPublicKey(String publicKeyStr) {
        try {
            byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新的加密方式
     * 区别于旧加密方式
     * 字段：isTrue 无用，区分重载的旧方法
     *
     * @param data
     * @param privateKey
     * @param isTrue
     * @return
     * @throws Exception
     */
    public final static String decrypt(String data, String privateKey, Boolean isTrue) throws Exception {
        return decrypt(data, getPrivateKey(privateKey), isTrue);
    }

    /**
     * 新的解密方式
     * 区别于旧解密方式
     * 字段：isTrue 无用，区分重载的旧方法
     *
     * @param text
     * @param publicKey
     * @param isTrue
     * @return
     */
    public final static String encrypt(String text, String publicKey, Boolean isTrue) {
        return encrypt(text, getPublicKey(publicKey), isTrue);
    }

    @Deprecated
    public final static String decrypt(String data, String privateKey) throws Exception {
        return decrypt(data, getPrivateKey(privateKey));
    }

    @Deprecated
    public final static String encrypt(String text, String publicKey) {
        return encrypt(text, getPublicKey(publicKey));
    }

    public static void main(String args[]) {

        try {
//            String plaintext = "{\"acceptDate\":\"2019-03-19 10:08:34\",\"coverages\":\"[]\",\"effectiveDate\":\"2019-03-20 00:00:00\",\"expiryDate\":\"2020-03-19 23:59:59\",\"insuranceTypeCode\":\"2\",\"insurantCertificateNo\":\"510101198001010090\",\"insurantName\":\"测试\",\"insureFlag\":\"1\",\"insurePlace\":\"四川省-成都市\",\"isImportCar\":\"0\",\"isLoanCar\":\"0\",\"isNew\":\"1\",\"lastYearClaimTimes\":\"0\",\"offInsureFlag\":\"N\",\"orderNo\":\"C1017150029\",\"partnerName\":\"天道测试\",\"paymentWay\":\"4\",\"policyHolderCertificateNo\":\"510101198001010090\",\"policyHolderName\":\"测试\",\"policyNo\":\"12600003900155541340\",\"premium\":\"950\",\"taxPremium\":\"300.0\",\"userCode\":\"ald27345364379\",\"userId\":\"1814\",\"userName\":\"123\",\"vehicleAcquisitionPrice\":\"221800\",\"vehicleAge\":\"1\",\"vehicleBrand\":\"一汽大众\",\"vehicleEngineNo\":\"GFDVCXDSF\",\"vehicleLicencePlateNo\":\"鲁A37285\",\"vehicleOwnerCertificateNo\":\"510101198001010090\",\"vehicleOwnerName\":\"测试\",\"vehicleRegisterDate\":\"2019-03-19 00:00:00\",\"vehicleTransferflag\":\"0\",\"vehicleType\":\"11\",\"vehicleUsageType\":\"2\",\"vehicleVIN\":\"GFJCXMDSKFDSNCXSA\"}";
            String plaintext = "{\"groupInsuranceFlag\":\"G\",\"channelId\":\"1005\",\"planCode\":\"PN23C998001800000001\",\"payId\":\"100\",\"businessCode\":\"A10002\",\"orderNo\":\"201812051808101005\",\"effectiveTime\":\"2018-12-15 00:00:00\",\"expiryTime\":\"2018-12-15 23:59:59\"}";
            String prk = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMP+AatiOGtmc1w9YHKnFFXlGZbmLHe6InTMG8uOwysAOg+I2KuAVO1Bo/CzpqKpu1JYuWNtZRHJY6RqLKRZq43vQeAUKCkWlVNaRcdhBPl4NFdhdw9PWJQW9bt/MY0fEt5CGXrXJnSUiYrNpITb/XcHj6EaEMoXg1GQUpSD/nTHAgMBAAECgYA6WtGybp8v0yQ77YZe/MJARONWm5fX2hLSRcds10fi8MFYyL0Ca0ZeEfI3A7Z0ria5APPJ19OE9Tp2xKogERbTuEP2q3amgwq3zqBS9+1Ckk9UPWZMh8Qm8G1kWhM6Aq3uHteoaye42Xechg1VNmjcGFAJuYyIfnvtprd5In8WWQJBAP9xHCgnHhP8xuH2VEgbSK0TkzjgkrJQz/8AeDKg1E9JftyPKzu8JXsyjIHot2/8/Vt59g2NelFfILkHPzwUt2MCQQDEa6QxhRc3HC2f+AAnib3QSQXMe290J34iDkIjcPwdoAkttO7C/q1H9dgH4bN5l7kQK9bs/yHr11XQ+BQ4z0RNAkB2oC2mS/xA5fvqhf2/+sO2dMfC+y/FFZhyYOJxCJVwOL5saSR3ujlhzCjKT6uYm1GBgNMOmiw6y8Wjh8UK/sfnAkA92dt5UHyP+QqQ7go2aRRhay0tR80/2VPJq42+FUz+ky9wuiHZO1MbbtnZGYRnjJRU+EkUlf57gnJefE/516KdAkA8Uz/V8S0bChyUVsJ43qvJfNKwVNBS1XygQmfDtnql1VpEq8srlgUQpsLeW7ypcQlLEB8vWFqvSQ32hKszTqSp";
            String puk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDD/gGrYjhrZnNcPWBypxRV5RmW5ix3uiJ0zBvLjsMrADoPiNirgFTtQaPws6aiqbtSWLljbWURyWOkaiykWauN70HgFCgpFpVTWkXHYQT5eDRXYXcPT1iUFvW7fzGNHxLeQhl61yZ0lImKzaSE2/13B4+hGhDKF4NRkFKUg/50xwIDAQAB";
            System.out.println("原始cipherText:" + plaintext);
            String cipherText = encrypt(plaintext, puk, true);
            System.out.println("加密cipherText:" + cipherText);
//            cipherText = "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";
            String plainText = decrypt(cipherText, prk, true);
            System.out.println("解密plainTex t:" + plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            String plaintext = "";
//            plaintext = "{\"applicant\":{\"address\":\"宁夏回族自治区银川市金凤区新昌西路53号\",\"areaCode\":\"110102\",\"cardNo\":\"51640000MJX1503580\",\"cardType\":\"8\",\"customerIndustryClassification\":\"44\",\"customerIndustryType\":\"D\",\"email\":\"slave2@126.com\",\"mobile\":\"13584041065\",\"name\":\"宁夏山地运动协会\",\"relationship\":\"5\"},\"batch\":\"2018080319330500001\",\"billType\":1,\"businessCode\":\"A10001\",\"channelId\":\"1005\",\"count\":1,\"effectiveTime\":\"2018-08-31 00:00:00\",\"expiryTime\":\"2018-09-01 00:00:00\",\"factors\":[{\"factorCode\":\"SP_25\",\"factorValue\":\"200\"}],\"groupInsuranceFlag\":\"S\",\"planCode\":\"PN23C998001800000001\",\"totalAmount\":100000.00,\"totalPremium\":18000,\"uid\":\"1\"}";
//            plaintext = "{\"uid\":\"QWEQ1\",\"businessCode\":\"A10001\",\"batch\":\"QWHEIUQWMNCSA12241\",\"planCode\":\"PN110701001800000032\",    \"billType\":1,    \"count\":1,    \"effectiveTime\":\"2018-10-30 00:00:00\",    \"expiryTime\":\"2019-10-30 00:00:00\",    \"totalAmount\":100000,    \"totalPremium\":1000,    \"groupInsuranceFlag\":\"S\",    \"applicant\":{        \"name\":\"宋野\",        \"cardType\":1,        \"cardNo\":\"140729199410290294\",        \"gender\":1,        \"address\":\"北京市\",        \"birthday\":\"1994-10-29\",        \"relationship\":1,        \"mobile\":\"17688888888\",        \"email\":\"123@qq.com\",        \"customerIndustryType\":1,        \"areaCode\":\"1231\"    },    \"insureds\":[        {            \"insuredCode\":\"1\",            \"name\":\"宋野\",            \"namePinYin\":\"zhangsan\",            \"cardType\":1,            \"cardNo\":\"140729199410290294\",            \"mobile\":\"17888888888\",            \"birthday\":\"1994-10-29\",            \"address\":\"北京\",            \"gender\":1,            \"email\":\"123@qq.com\",            \"benifitMark\":1        }    ],    \"beneficiarys\":[        {            \"insuredCode\":1,            \"name\":\"宋野\",            \"cardType\":1,            \"cardNo\":\"140729199410290294\",            \"mobile\":\"17888888888\",            \"birthday\":\"1994-10-29\",            \"address\":\"北京\",            \"gender\":1,            \"email\":\"123@qq.com\",            \"benifitProportion\":1,            \"benifitOrder\":1        }    ],\"extendItems\":[{\"extend001\":\"100\",\"extend002\":\"365\"}],\"ppublicspec\":{\"businessid\":\"hahha\"}}";
//            plaintext = "{\"policyNo\":\"ABEJ600E2018PAAAAT9T\"}";
//            plaintext = "{\"policyNo\":\"\",\"orderNo\":\"15380140177211005\",\"batch\":\"\",\"applyNo\":\"\",\"queryType\":0}";
//            plaintext = "{\"uid\":\"QWEQ1\",\"businessCode\":\"A10001\",\"batch\":\"QWHEIUQWMNCSA12241\",\"planCode\":\"P23C598001800001161\",    \"billType\":1,    \"count\":1,    \"effectiveTime\":\"2018-12-30 00:00:00\",    \"expiryTime\":\"2019-06-01 00:00:00\",    \"totalAmount\":100000,    \"totalPremium\":1000,    \"groupInsuranceFlag\":\"G\",    \"applicant\":{        \"name\":\"宋野\",        \"cardType\":1,        \"cardNo\":\"140729199410290294\",        \"gender\":1,        \"address\":\"北京市\",        \"birthday\":\"1994-10-29\",        \"relationship\":1,        \"mobile\":\"17688888888\",        \"email\":\"123@qq.com\",        \"customerIndustryType\":1,        \"areaCode\":\"1231\"    },    \"insureds\":[        {            \"insuredCode\":\"1\",            \"name\":\"宋野\",            \"namePinYin\":\"zhangsan\",            \"cardType\":1,            \"cardNo\":\"110101201003074370\",            \"mobile\":\"17888888888\",            \"birthday\":\"2010-03-07\",            \"address\":\"北京\",            \"gender\":1,            \"email\":\"123@qq.com\",            \"benifitMark\":1        }    ],    \"beneficiarys\":[        {            \"insuredCode\":1,            \"name\":\"宋野\",            \"cardType\":1,            \"cardNo\":\"140729199410290294\",            \"mobile\":\"17888888888\",            \"birthday\":\"1994-10-29\",            \"address\":\"北京\",            \"gender\":1,            \"email\":\"123@qq.com\",            \"benifitProportion\":1,            \"benifitOrder\":1        }    ]}";
//            plaintext = "{\"uid\":\"QWEQ1\",\"businessCode\":\"A10001\",\"batch\":\"QWHEIUQWMNCSA12241\",\"planCode\":\"PN23C998001800000001\",    \"billType\":1,    \"count\":1,    \"effectiveTime\":\"2018-12-15 00:00:00\",    \"expiryTime\":\"2018-12-15 23:59:59\",    \"totalAmount\":100000,    \"totalPremium\":1000,    \"groupInsuranceFlag\":\"G\",    \"applicant\":{        \"name\":\"宋野\",        \"cardType\":1,        \"cardNo\":\"140729199410290294\",        \"gender\":1,        \"address\":\"北京市\",        \"birthday\":\"1994-10-29\",        \"relationship\":1,        \"mobile\":\"17688888888\",        \"email\":\"123@qq.com\",        \"customerIndustryType\":1,        \"areaCode\":\"1231\"    },    \"insureds\":[        {            \"insuredCode\":\"1\",            \"name\":\"宋野\",            \"namePinYin\":\"zhangsan\",            \"cardType\":1,            \"cardNo\":\"140729199410290294\",            \"mobile\":\"17888888888\",            \"birthday\":\"1994-10-29\",            \"address\":\"北京\",            \"gender\":1,            \"email\":\"123@qq.com\",            \"benifitMark\":1        }    ],    \"beneficiarys\":[        {            \"insuredCode\":1,            \"name\":\"宋野\",            \"cardType\":1,            \"cardNo\":\"140729199410290294\",            \"mobile\":\"17888888888\",            \"birthday\":\"1994-10-29\",            \"address\":\"北京\",            \"gender\":1,            \"email\":\"123@qq.com\",            \"benifitProportion\":1,            \"benifitOrder\":1        }    ],\"factors\":[{\"factorCode\":\"SP_41\",\"factorValue\":\"200人以内\"},{\"factorCode\":\"SP_25\",\"factorValue\":\"199\"}]}";
//            plaintext = "{\"groupInsuranceFlag\":\"G\",\"channelId\":\"1005\",\"planCode\":\"PN23C998001800000001\",\"payId\":\"100\",\"businessCode\":\"A10002\",\"orderNo\":\"201812051808101005\",\"effectiveTime\":\"2018-12-15 00:00:00\",\"expiryTime\":\"2018-12-15 23:59:59\"}";
//            String prk = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKXej+UIOQiabhKHBjlypgdas11LwYP2Y0zgZt+aLuIooovpzXaRUWaVszEczaLQJbVjgL5x6BL36dlba5ivIcyg1QVBRqkQkiMB3p4CHcK0DWjMEIYIVuOP7S+Hs01oIxJd4VcX2dOsXMHN6a8XwxJP7mDpgobeqjZDqCRGVBBNAgMBAAECgYAEsJdG9YWPTbaBBZpSc4BoQtNBdwvgrE3AdbPS7KwYrudxp0ms6YzwppjrcZIK4XG/gDG6sMPMDYBqKeCP75u3/skL0f+4Lk0Hw5s/2HbM0P0+DCPdwKZ5LBOmOlbZ5LDJkWw7MrEclbClixjPKlPAVRgWhtihNmKMDOgmgGjGbQJBAM5G0P321QHMmaHXSb54q7XpOoWIb8gXG85ns+qzxQ6+doFyAr4bFN/fs96xHsPDKM5XehKfVDwW+hlaUnjzum8CQQDN2j5Xi8MzNxcr8UzX7vaxmIBsrJMSNmkLUgwf0cV6CqJ2pJJBHGAwwa9Vi2uPauQAW2jaCx3Fj7iUYPdw6a8DAkBuPmbf9JWvGnxZhMhC5hdPZ9EBIQQs+jzAOahsnZuJ371SPW69z5d+dv6Y3CzHD3y1mwcBAOpyGw2bkyxUvF49AkAdFfUxEYJnPiV8BABDknQw68dZk5jEPgPhoZos9MreT5pdASvZH1VIDlXj1GtCPGwy0Lej92YhgAkojfrobVxZAkBagyPNEWlN4/FMFTdOj6pGm6Pa1Ayiemqe+A76JdAoXkAhCMM5nihLzX0tC41AG5dg6SjAdiElUNXYiHUPvWEB";
//            String puk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5I+O675j8HtAj5m2blrQDSLRTOBr163Z3/H2daQAzf3rHeYO+wHU6F+XzueDfQkbTzq3FDCf/3dIJrMH8NkTHUfCFW1oyzxgn8pOIw5YC59HJ6eCcAugQmTfIbrYUjnShlT8vixFSEfxUy7/XYe9nwGS0MiIfSArhy7huoLzFvwIDAQAB";
//            puk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCl3o/lCDkImm4ShwY5cqYHWrNdS8GD9mNM4Gbfmi7iKKKL6c12kVFmlbMxHM2i0CW1Y4C+cegS9+nZW2uYryHMoNUFQUapEJIjAd6eAh3CtA1ozBCGCFbjj+0vh7NNaCMSXeFXF9nTrFzBzemvF8MST+5g6YKG3qo2Q6gkRlQQTQIDAQAB";
//            System.out.println("cipherText:" + plaintext);
//            String cipherText = encrypt(plaintext, puk);
//            System.out.println("cipherText:" + cipherText);
//            cipherText = "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";
//            String plainText = decrypt(cipherText, prk);
//
//            System.out.println("plainTex t:" + plainText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

}