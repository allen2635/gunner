package com.allen.util;

import java.util.HashMap;
import java.util.Map;

public class HttpParameter {

    private Map<String, Object> map;

    public HttpParameter(boolean isSuccess, String message, Map<String, Object> result) {

        map = new HashMap<String, Object>();
        map.put("code", isSuccess ? 200 : 400);
        map.put("message", message == null ? (isSuccess ? "请求成功" : "请求失败") : message);
        map.put("result", result);
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(map);
    }
}
