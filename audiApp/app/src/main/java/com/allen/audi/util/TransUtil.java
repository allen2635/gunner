package com.allen.audi.util;

import org.json.JSONObject;

import java.util.Map;

public class TransUtil {

    /**
     * 将TransData转出为JSON字符串
     *
     * @param map 参数名-值
     * @return JSON数据
     */
    public static String listToJson(Map<String, String> map) {
        JSONObject json = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 将JSON字符串转成TransData
     *
     * @param result json字符
     * @return TransData
     */
    public static TransData getResponse(String result) {
        TransData response = null;
        if (StringUtil.isNotEmpty(result)) {
            try {
                response = new TransData();
                JSONObject jsonObject = new JSONObject(result);
                response.setCode(jsonObject.optString("code"));
                response.setResult(jsonObject.optString("result"));
                response.setMessage(jsonObject.optString("message"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

}
