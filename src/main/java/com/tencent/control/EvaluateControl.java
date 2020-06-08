package com.tencent.control;

import com.alibaba.fastjson.JSON;
import com.tencent.pojo.User;
import com.tencent.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class EvaluateControl {
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private HttpServletRequest request;

    //添加评论
    @RequestMapping("/evaluate")
    public String addEvaluate(String content,int newsid){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "login";
        }else{
            int n = evaluateService.addEvaluate(user.getUserid(),newsid,content);
            if(n == 0){
                return "over";
            }else if(n>0){
                return "ok";
            }else{
                return "no";
            }
        }
    }

    //根据newsid显示评论
    @RequestMapping("/showEvaluate")
    public String showEvaluate(int newsid){
        List list = evaluateService.queryEvaluateByNewsId(newsid);
        String pl = JSON.toJSONString(list);
//        System.out.println(pl.toString());
        return pl;
    }

    //根据userid查询评论
    @RequestMapping("/queryEvaluateByUserId")
    public String queryEvaluateByUserId(){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return null;
        }else{
            List list = evaluateService.queryEvaluateByUserId(user.getUserid());
            String s = JSON.toJSONString(list);
            return s;
        }
    }

    //删除评论
    @RequestMapping("/deleteEvaluate")
    public String deleteEvaluate(int newsid){
        User user = (User) request.getSession().getAttribute("user");
        //登录后才可以点击,无需判断user是否为空
        int n = evaluateService.deleteEvaluate(user.getUserid(),newsid);
        if(n>0){
            return "ok";
        }else{
            return "no";
        }
    }
}
