package com.tencent.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.pojo.User;
import com.tencent.service.UserService;
import com.tencent.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserControl {
    @Autowired
    private UserService userService;
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/yzm")
    public String sendYZM(String mobile){
        int n = (int) Math.round(Math.random()*10000);
        String code = String.valueOf(n);
        System.out.println(code);
        if(mobile != null){
//            smsUtil.SendSms(mobile,code);
            return code;
        }else{
            return "failure";
        }
    }
    //注册
    @RequestMapping("/regist")
    public String regist(String username,String password,String mobile){
        int n = userService.regist(username,password,mobile);
        if(n<0){
            return "failure";
        }else if(n==0){
            return "exist";
        }else{
            return "success";
        }
    }
    //登录
    @RequestMapping("/login")
    public String login(String username,String password){
        User user = userService.login(username,password);
        if(user!=null){
            request.getSession().setAttribute("user",user);
            return "success";
        }else{
            return "failure";
        }
    }
    //获取session中的user
    @RequestMapping("/getSession")
    public String getSession(){
        User user = (User) request.getSession().getAttribute("user");
        if(user != null){
            return user.getUsername();
        }else {
            return null;
        }
    }
}
