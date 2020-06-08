package com.tencent.control;

import com.alibaba.fastjson.JSON;
import com.tencent.pojo.News;
import com.tencent.pojo.User;
import com.tencent.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NewsControl {
    @Autowired
    private NewsService newsService;
    @Autowired
    private HttpServletRequest request;

    //搜狗搜索分页查询
    @RequestMapping("/search")
    public String search(String text){
        List<News> list = newsService.search(text);
        String news = JSON.toJSONString(list);
        return news;
    }

    //根据id查询新闻
    @RequestMapping("querynews")
    public String queryNews(int newsid){
        News news = newsService.queryNews(newsid);
        String news1 = JSON.toJSONString(news);

        //获取用户id
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            //用户不存在，不记录
            return news1;
        }else{
            //用户存在，记录
            newsService.saveRecord(user.getUserid(),newsid);

            return news1;
        }
    }
}
