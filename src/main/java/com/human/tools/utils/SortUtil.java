package com.human.tools.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.*;

/**
 * 示例：<排序工具类>
 *
 * @author shaonan.hu
 * @version V1.0
 * @Time 2019/4/9
 */
public class SortUtil {

    /**
     * @param @param  paraMap
     * @param @param  urlEncode
     * @param @param  keyToLower
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: formatParamMap
     * @Description: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），生成需要MD5加密的url参数串
     * @date 2017年12月7日 上午11:28:03
     */
    public static String formatParamMap(String paraMap) {
        String buff = "";
        Map<String, String> tmpMap = null;
        try {
            tmpMap = (Map) JSON.parse(paraMap);
        } catch (Exception e) {
            return "入参格式不为json";
        }
        try {
            List<Map.Entry<String, String>> infoIds =
                    new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {


                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();

                    val = URLEncoder.encode(val, "utf-8");

                    buf.append(key + "=" + val);

                    buf.append("&");
                }
            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }


    public static void main(String args[]) {

        //测试签名
        byte[] sign = null;
        try {
            JSONObject map = new JSONObject();
            map.put("msg", "xxx");
            map.put("code", "200");
            map.put("data", "hhh");
            String s = formatParamMap(map.toJSONString());
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
