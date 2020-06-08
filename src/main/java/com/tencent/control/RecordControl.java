package com.tencent.control;

import com.alibaba.fastjson.JSON;
import com.tencent.pojo.User;
import com.tencent.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RecordControl {
    @Autowired
    private RecordService recordService;
    @Autowired
    private HttpServletRequest request;

    //协同过滤算法推荐
    @RequestMapping("/xtgl")
    public String xtgl(){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            List list = recordService.noLoginTJ();
            String s = JSON.toJSONString(list);
//            System.out.println(s.toString());
            return s;
        }else{
            int userid = user.getUserid();
            //得到推荐新闻集合
            List list = recordService.xtgl(userid);
            if(list.isEmpty()){
                List list1 = recordService.noLoginTJ();
                String s = JSON.toJSONString(list1);
//                System.out.println(s.toString());
                return s;
            }else{
                String s = JSON.toJSONString(list);
//                System.out.println(s.toString());
                return s;
            }
        }
    }

    //热搜算法
    @RequestMapping("/rs")
    public String rs(){
        List list = recordService.rs();
        String s = JSON.toJSONString(list);
//        System.out.println(s.toString());
        return s;
    }
}
