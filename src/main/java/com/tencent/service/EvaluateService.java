package com.tencent.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.dao.EvaluateDAO;
import com.tencent.dao.NewsDAO;
import com.tencent.dao.UserDAO;
import com.tencent.pojo.Evaluate;
import com.tencent.pojo.News;
import com.tencent.pojo.User;
import com.tencent.vo.NewsEvaluate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluateService {
    @Autowired
    private EvaluateDAO evaluateDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private NewsDAO newsDAO;

    //添加评论
    public int addEvaluate(int userid,int newsid,String content){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        queryWrapper.eq("newsid",newsid);
        int n = evaluateDAO.selectCount(queryWrapper);

        if(n>0){
            return 0;
        }else{
            Evaluate evaluate = new Evaluate();
            evaluate.setUserid(userid);
            evaluate.setNewsid(newsid);
            evaluate.setContent(content);

            int m = evaluateDAO.insert(evaluate);
            return m;
        }
    }

    //根据newsid查评论
    public List queryEvaluateByNewsId(int newsid){
        List pl = new ArrayList();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("newsid",newsid);

        List list = evaluateDAO.selectList(queryWrapper);
        for(int i=0;i<list.size();i++){
            Evaluate evaluate = (Evaluate) list.get(i);
            //新建一个中间模型对象，用来存放显示数据
            NewsEvaluate newsEvaluate = new NewsEvaluate();
            newsEvaluate.setNewsid(evaluate.getNewsid());
            newsEvaluate.setContent(evaluate.getContent());

            //根据userid查username
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("userid",evaluate.getUserid());
            User user = userDAO.selectOne(wrapper1);
            newsEvaluate.setUsername(user.getUsername());

            //根据newsid查title
            QueryWrapper wrapper2 = new QueryWrapper();
            wrapper2.eq("newsid",evaluate.getNewsid());
            News news = newsDAO.selectOne(wrapper2);
            newsEvaluate.setTitle(news.getTitle());

            pl.add(newsEvaluate);
        }
        return pl;
    }

    //根据userid查评论
    public List queryEvaluateByUserId(int userid){
        //返回数据
        List pl = new ArrayList();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        List list = evaluateDAO.selectList(queryWrapper);

        for(int i=0;i<list.size();i++){
            Evaluate evaluate = (Evaluate) list.get(i);
            //返回的vo模型
            NewsEvaluate newsEvaluate = new NewsEvaluate();
            newsEvaluate.setNewsid(evaluate.getNewsid());
            newsEvaluate.setContent(evaluate.getContent());

            //根据newsid查title
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("newsid",evaluate.getNewsid());
            News news = newsDAO.selectOne(wrapper);
            newsEvaluate.setTitle(news.getTitle());

            pl.add(newsEvaluate);
        }
        return pl;
    }

    //删除评论
    public int deleteEvaluate(int userid,int newsid){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        queryWrapper.eq("newsid",newsid);

        int n = evaluateDAO.delete(queryWrapper);
        return n;
    }
}
