package com.human.tools.data_security;

import com.human.tools.utils.DecriptUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * http工具类，支持GET/POST/PUT/DELETE
 *
 * @author shaonan.hu
 * @version V1.0
 * @Time 2019/4/9
 */

@Service
@PropertySource(value = "classpath:spring-tool-config.properties")
public class DataSecurityService {

    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Value("${pip.tool.dataSecurityKey}")
    private String dataSecurityKey;
    private String markStr="*";

//    public static void main(String[] args) throws Exception {
//        DataSecurityService d=new DataSecurityService();
//        System.out.println(d.showDRight4("187003325643","",0));
//        System.out.println(d.showDisEncryptLeft2right4("1103434332123421156",""));
//        String ss=d.idCardNoEncryptECB("1103434332123421156");
//        System.out.println(d.showEncryptLeft2right4(ss,""));
//    }

    /**
     * 加密证件号
     */
    public String idCardNoEncryptECB(String dataStr){

        try {
            return DecriptUtils.encryptECB(dataSecurityKey, dataStr);
        }catch (Exception e) {
            logger.debug("加密失败:",e);
            return dataStr;
        }
    }

    /**
     * 解密证件号
     */
    public String idCardDecryptECB(String encryptECBDataStr) {
        try {
            return DecriptUtils.decryptECB(dataSecurityKey, encryptECBDataStr);
        } catch (Exception e) {
            logger.debug("解密失败:",e);
            return dataSecurityKey;
        }
    }

    /**
     * 通过符号替代显示
     */
    public String showDRight4(String dataStr,String markStr,int leftLength){
        if(StringUtils.isNotBlank(dataStr) && dataStr.length()>(leftLength+4)){
            int size=dataStr.length()-(leftLength+4);
            if(StringUtils.isNotBlank(markStr)){
                this.markStr=markStr;
            }
            String tempMarkStr="";
            for(int i=0;i<size;i++){
                tempMarkStr+=this.markStr;
            }
            if(leftLength>0){
                dataStr=dataStr.substring(0,leftLength)+tempMarkStr+dataStr.substring(dataStr.length()-4);
            }else{
                dataStr=tempMarkStr+dataStr.substring(dataStr.length()-4);

            }
        }

        return dataStr;
    }

    /**
     * 通过符号替代显示
     * @param encryptECBDataStr 为加密证件号
     * @param markStr 字符
     * @return
     * @throws Exception
     */
    public String showDisEncryptLeft2right4(String encryptECBDataStr,String markStr) {
        return showDRight4(encryptECBDataStr,markStr,2);
    }
    /**
     * 通过符号替代显示
     * @param encryptECBDataStr 为加密证件号
     * @param markStr 字符
     * @return
     * @throws Exception
     */
    public String showEncryptLeft2right4(String encryptECBDataStr,String markStr){
        return showDRight4(idCardDecryptECB(encryptECBDataStr),markStr,2);
    }


}
