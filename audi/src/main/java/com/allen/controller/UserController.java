package com.allen.controller;

import com.allen.model.User;
import com.allen.service.IUserService;
import com.allen.util.GsonUtils;
import com.allen.util.HttpParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/user", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password) {

        User user = userService.queryUser(username, password);
        HttpParameter parameter;
        Map<String, Object> result = null;
        boolean isSuccess = user != null;
        if (isSuccess) {

            result = new HashMap<String, Object>();
            result.put("userId", user.getUserid());
            result.put("username", user.getUsername());
            result.put("nickname", user.getNickname());
        }
        parameter = new HttpParameter(isSuccess, null, result);
        log.info(parameter.toString());
        return parameter.toString();
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public String test(@RequestParam(value = "username") String username,
                       @RequestParam(value = "password") String password) {

        User user = userService.queryUser(username, password);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", user.getUserid());
        map.put("username", user.getUsername());
        map.put("nickname", user.getNickname());
        return GsonUtils.toJson(map);
    }
}
