package com.allen.controller;

import com.allen.util.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/user", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class UserController {

    @RequestMapping(value = "/getUserByGet", method = RequestMethod.GET)
    @ResponseBody
    public String getUserByGet(@RequestParam(value = "userid") String userid) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", userid);
        map.put("message", "成功");
        Map<String, Object> result = new HashMap<String, Object>();
        String[] ss = new String[2];
        ss[0] = "1";
        ss[1] = "2";
        result.put("list", ss);
        map.put("result", result);
        return GsonUtils.toJson(map);
    }
}
