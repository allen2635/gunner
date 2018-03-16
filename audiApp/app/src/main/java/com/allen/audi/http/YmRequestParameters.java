package com.allen.audi.http;


import com.allen.audi.util.StringUtil;
import com.allen.audi.util.TransUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @className: YmRequestParameters
 * @classDescription: This class keep the request parameters and convert the parameters
 * to a json then encrypt it.
 * @author: swallow
 * @createTime: 2015/6/11
 */
public class YmRequestParameters {
    private Map<String, String> paramMap;


    public YmRequestParameters(String[] paramKey, String... paramValue) {
        paramMap = new HashMap<>();
        for (int i = 0; paramKey != null && i < paramKey.length && i < paramValue.length; i++) {
            paramMap.put(paramKey[i], paramValue[i]);
        }
    }


    @Override
    public String toString() {
        String strJson = TransUtil.listToJson(paramMap);
        if (StringUtil.isNotEmpty(strJson)) {
//            String paramTea = Tea.encryptByBase64Tea(strJson); // 加密json数据
            paramMap.clear();
            paramMap = null;
            return strJson;
        }
        return "Error: " + this.getClass().getName();
    }
}
