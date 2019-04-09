package com.human.tools.utils.http;

import com.human.tools.utils.AESUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.*;

/**
 * <>封装调用第三方接口的工具类</>
 * <>是对httpUtils的使用，用于调用接口时方便，类名自拟</>
 *
 * @author shaonan.hu
 * @version V1.0
 * @Time 2019/4/9
 */
public class xxxClient {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // 开发者平台申请的APP ID
    private static final String APP_ID = "xxxxxx";//由第三方成功
    // 开发者平台申请的APP Secret
    private static final String APP_SECRET = "xxxxxx"; //由第三方成功
    // 访问域名
    private static final String COMMON_URL = "https://xxxx/";//由第三方提供


    /**
     * GET请求
     *
     * @param methodUrl
     * @param requestParams
     * @return
     */
    public String executeGet(String methodUrl, TreeMap<String, Object> requestParams) {
        try {
            ParamsData paramsData = getParamData(methodUrl, requestParams, "");
            logger.info("Request GET Url ==>>{}", paramsData.getRequestUrl());
            String result = HttpUtils.doGet2String(paramsData.getRequestUrl(), paramsData.getHeaders(), "");
            return AESUtil.aesDecrypt(result, APP_SECRET);
        } catch (Exception e) {
            e.printStackTrace();
            return "请求xxx服务异常：" + e.getMessage();
        }
    }


    /**
     * POST请求
     *
     * @param methodUrl
     * @param body
     * @return
     */
    public String executePost(String methodUrl, String body) {
        try {
            ParamsData paramsData = getParamData(methodUrl, null, body);
            logger.info("Request POST Url ==>>{}", paramsData.getRequestUrl());
            String postData = AESUtil.aesEncrypt(body, APP_SECRET);//对body进行加密
            if (StringUtils.isBlank(postData)) {
                postData = "";
            }
            String result = HttpUtils.doPost2String(paramsData.getRequestUrl(), postData, paramsData.getHeaders(), "UTF-8");
            return AESUtil.aesDecrypt(result, APP_SECRET);
        } catch (Exception e) {
            e.printStackTrace();
            return "请求xxx服务异常：" + e.getMessage();
        }
    }

    /**
     * PUT请求
     *
     * @param methodUrl
     * @param body
     * @return
     * @throws Exception
     */
    public String executePut(String methodUrl, String body) throws Exception {
        try {
            ParamsData paramsData = getParamData(methodUrl, null, body);
            logger.info("Request Put Url ==>>{}", paramsData.getRequestUrl());
            String postData = AESUtil.aesEncrypt(body, APP_SECRET);//对body进行加密
            if (StringUtils.isBlank(postData)) {
                postData = "";
            }
            String result = HttpUtils.doPut2String(paramsData.getRequestUrl(), postData, paramsData.getHeaders(), "");
            return AESUtil.aesDecrypt(result, APP_SECRET);
        } catch (Exception e) {
            e.printStackTrace();
            return "请求xxx服务异常：" + e.getMessage();
        }
    }

    /**
     * DELETE请求
     *
     * @param methodUrl1
     * @param requestParams
     * @return
     */
    public String executeDelete(String methodUrl1, TreeMap<String, Object> requestParams) {
        try {
            ParamsData paramsData = getParamData(methodUrl1, requestParams, "");
            logger.info("Request DELETE Url ==>>{}", paramsData.getRequestUrl());
            String result = HttpUtils.doDelete2String(paramsData.getRequestUrl(), "", paramsData.getHeaders(), "");
            return AESUtil.aesDecrypt(result, APP_SECRET);
        } catch (Exception e) {
            e.printStackTrace();
            return "请求xxx服务异常：" + e.getMessage();
        }
    }

    /**
     * 封装Header
     *
     * @return
     */
    public List<Header> getHeaders() {
        BasicHeader header = new BasicHeader("x-appid", APP_ID);
        List<Header> headers = new ArrayList<Header>();
        headers.add(header);
        return headers;
    }

    /**
     * 根据参数的KEY按照字段顺序拼接
     *
     * @param requestParams
     * @return
     */
    private String convertRequestParams(TreeMap<String, Object> requestParams) {
        String requestP = "";
        Iterator iter = requestParams.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            requestP += entry.getKey().toString() + entry.getValue();
        }
        logger.info("根据Key拼接后的参数:" + requestP);
        return requestP;
    }

    private ParamsData getParamData(String methodUrl, TreeMap<String, Object> requestParams, String body) {
        String url = null;
        String params = "";
        ParamsData paramsData = new ParamsData();
        try {
            if (null == requestParams) {
                requestParams = new TreeMap<>();
            } else {
                //按照key的字典顺序转换
                params = convertRequestParams(requestParams);
            }

            body = AESUtil.aesEncrypt(body, APP_SECRET);
            String securitySign = AESUtil.md5(APP_ID, params, body);
            requestParams.put("securitySign", securitySign);

            // 构建请求参数
            StringBuffer paramSb = new StringBuffer();
            if (requestParams != null) {
                for (java.util.Map.Entry<String, Object> e : requestParams.entrySet()) {
                    paramSb.append(e.getKey());
                    paramSb.append("=");
                    // 将参数值urlEncode编码,防止传递中乱码
                    paramSb.append(URLEncoder.encode(String.valueOf(e.getValue()), "UTF-8"));
                    paramSb.append("&");
                }
                paramSb.substring(0, paramSb.length() - 1);
            }
            url = COMMON_URL + methodUrl + "?" + paramSb.toString();
            paramsData.setRequestUrl(url);
            paramsData.setHeaders(getHeaders());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramsData;
    }

    class ParamsData {
        private String md5;
        private String sign;
        private String requestUrl;
        private List<Header> headers;

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public void setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
        }

        public List<Header> getHeaders() {
            return headers;
        }

        public void setHeaders(List<Header> headers) {
            this.headers = headers;
        }
    }
}
