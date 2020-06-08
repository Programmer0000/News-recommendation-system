package com.tencent.control;

import com.alibaba.fastjson.JSON;
import com.tencent.pojo.User;
import com.tencent.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CollectionControl {
    @Autowired
    private CollectionService collectionService;
    @Autowired
    private HttpServletRequest request;

    //新增收藏
    @RequestMapping("/collection")
    public String addCollection(int newsid){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "false";
        }else{
            boolean b = collectionService.existCollection(user.getUserid(),newsid);
            if(b){
                return "hascollection";
            }else{
                //进行收藏
                int n = collectionService.addCollection(user.getUserid(),newsid);
                if(n>0){
                    return "yes";//收藏成功
                }else{
                    return "no";//收藏失败
                }
            }
        }
    }

    //通过userid查询收藏
    @RequestMapping("/queryCollectionByUserId")
    public String queryCollectionByUserId(){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return null;
        }else{
            List sc = collectionService.queryCollectionByUserId(user.getUserid());
            String s = JSON.toJSONString(sc);
            return s;
        }
    }

    //删除收藏
    @RequestMapping("/deleteCollection")
    public String deleteCollection(int newsid){
        User user = (User) request.getSession().getAttribute("user");
        int n = collectionService.deleteCollection(user.getUserid(),newsid);
        if(n>0){
            return "ok";
        }else {
            return "no";
        }
    }
}
