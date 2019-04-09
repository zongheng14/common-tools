package com.human.tools.utils.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * http工具类，支持GET/POST/PUT/DELETE
 *
 * @author shaonan.hu
 * @version V1.0
 * @Time 2019/4/9
 */

public class HttpUtils {

    private static final String DefaultUserAgent = "KdtApiSdk Client v0.1";
    private static final String HTTPS = "https";
    private static final String APPLICATION_JSON = "application/json;charset=";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    private static RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(600000).setSocketTimeout(600000).setConnectTimeout(600000).build();

    public static String doGet2String(String requestUrl, List<Header> headers, String charset) throws Exception {
        if (StringUtils.isBlank(charset)) {
            charset = "utf-8";
        }
        HttpResponse response = doGet(requestUrl, headers, charset);
        return convertData2Str(response, charset);
    }

    public static HttpResponse doGet(String requestUrl, List<Header> headers, String charset) throws Exception {
        HttpClient httpClient = getHttpClient(requestUrl);

        // 发送get请求
        HttpGet httpGet = new HttpGet(requestUrl);
        httpGet.setConfig(requestConfig);
        httpGet.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON + charset);
        if (headers != null && headers.size() > 0) {
            for (Header h : headers) {
                httpGet.addHeader(h);
            }
        }
        return httpClient.execute(httpGet);
    }


    public static String doPost2String(String requestUrl, String content, List<Header> headers, String charset) throws Exception {
        if (StringUtils.isBlank(charset)) {
            charset = "utf-8";
        }
        HttpResponse response = doPost(requestUrl, content, false, headers, charset);
        return convertData2Str(response, charset);
    }

    public static HttpResponse doPost(String requestUrl, String content, boolean isDefault, List<Header> headers, String charset) throws Exception {
        HttpClient httpClient = getHttpClient(requestUrl);

        HttpPost httpPost = new HttpPost(requestUrl);
        httpPost.setConfig(requestConfig);
        if (isDefault) {
            httpPost.addHeader("User-Agent", DefaultUserAgent);
        } else {
            httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON + charset);
        }
        if (headers != null && headers.size() > 0) {
            for (Header h : headers) {
                httpPost.addHeader(h);
            }
        }

        StringEntity se = new StringEntity(content, charset);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON + charset));
        httpPost.setEntity(se);
        return httpClient.execute(httpPost);
    }

    public static String doPatch2String(String requestUrl, String content, List<Header> headers, String charset) throws Exception {
        if (StringUtils.isBlank(charset)) {
            charset = "utf-8";
        }
        HttpResponse response = doPatch(requestUrl, content, false, headers, charset);
        return convertData2Str(response, charset);

    }

    public static HttpResponse doPatch(String requestUrl, String content, boolean isDefault, List<Header> headers, String charset) throws Exception {
        HttpPatch httpPatch = new HttpPatch(requestUrl);
        httpPatch.setConfig(requestConfig);

        if (isDefault) {
            httpPatch.addHeader("User-Agent", DefaultUserAgent);
        } else {
            httpPatch.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON + charset);
        }
        if (headers != null && headers.size() > 0) {
            for (Header h : headers) {
                httpPatch.addHeader(h);
            }
        }

        StringEntity se = new StringEntity(content, charset);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON + charset));
        httpPatch.setEntity(se);
        HttpClient httpClient = getHttpClient(requestUrl);
        return httpClient.execute(httpPatch);

    }


    public static String doPut2String(String requestUrl, String content, List<Header> headers, String charset) throws Exception {
        if (StringUtils.isBlank(charset)) {
            charset = "utf-8";
        }
        HttpResponse response = doPut(requestUrl, content, false, headers, charset);
        return convertData2Str(response, charset);

    }

    public static HttpResponse doPut(String requestUrl, String content, boolean isDefault, List<Header> headers, String charset) throws Exception {

        HttpPut httpPut = new HttpPut(requestUrl);
        httpPut.setConfig(requestConfig);

        if (isDefault) {
            httpPut.addHeader("User-Agent", DefaultUserAgent);
        } else {
            httpPut.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON + charset);
        }
        if (headers != null && headers.size() > 0) {
            for (Header h : headers) {
                httpPut.addHeader(h);
            }
        }

        StringEntity se = new StringEntity(content, charset);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON + charset));
        httpPut.setEntity(se);
        HttpClient httpClient = getHttpClient(requestUrl);
        return httpClient.execute(httpPut);

    }

    public static String doDelete2String(String requestUrl, String content, List<Header> headers, String charset) throws Exception {
        if (StringUtils.isBlank(charset)) {
            charset = "utf-8";
        }
        HttpResponse response = doDelete(requestUrl, content, false, headers, charset);
        return convertData2Str(response, charset);
    }

    //    public static HttpResponse doDelete(String requestUrl, List<Header> headers,String charset) throws Exception {
//        HttpClient httpClient = getHttpClient(requestUrl);
//
//        // 发送get请求
//        HttpGet httpGet = new HttpGet(requestUrl);
//        httpGet.setConfig(requestConfig);
//        httpGet.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON+charset);
//        if (headers != null && headers.size() > 0) {
//            for (Header h : headers) {
//                httpGet.addHeader(h);
//            }
//        }
//        return httpClient.execute(httpGet);
//    }
    public static HttpResponse doDelete(String requestUrl, String content, boolean isDefault, List<Header> headers, String charset) throws Exception {

        HttpDelete httpDelete = new HttpDelete(requestUrl);
        httpDelete.setConfig(requestConfig);

        if (isDefault) {
            httpDelete.addHeader("User-Agent", DefaultUserAgent);
        } else {
            httpDelete.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON + charset);
        }
        if (headers != null && headers.size() > 0) {
            for (Header h : headers) {
                httpDelete.addHeader(h);
            }
        }

//        StringEntity se = new StringEntity(content, charset);
//        se.setContentType(CONTENT_TYPE_TEXT_JSON);
//        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON+charset));
//        httpDelete.setEntity(se);

        HttpClient httpClient = getHttpClient(requestUrl);
        return httpClient.execute(httpDelete);
    }


    public static String convertData2Str(HttpResponse response, String charset) throws Exception {
        String result = "";
        try {
            if (StringUtils.isBlank(charset)) {
                charset = "utf-8";
            }
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;


    }


    public static String getHttpBody(BufferedReader reader) {
        StringBuffer buffer = new StringBuffer();
        try {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                buffer.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return buffer.toString();
    }

    private static HttpClient getHttpClient(String requestUrl) throws Exception {
        HttpClient httpClient = null;
        if (isHttpsRequest(requestUrl)) {
            httpClient = new SSLClient();
        } else {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    //判断请求方式是否为https true：https方式
    private static boolean isHttpsRequest(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        if (url.toLowerCase().startsWith(HTTPS)) {
            return true;
        }
        return false;
    }


}
