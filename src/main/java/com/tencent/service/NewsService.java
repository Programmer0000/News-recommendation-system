package com.tencent.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.dao.NewsDAO;
import com.tencent.dao.RecordDAO;
import com.tencent.pojo.News;
import com.tencent.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RecordDAO recordDAO;

    public List<News> search(String text){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("title",text);
        queryWrapper.or();
        queryWrapper.like("author",text);
        queryWrapper.or();
        queryWrapper.like("typename",text);

        List<News> news = newsDAO.selectList(queryWrapper);
        return news;
    }

    public News queryNews(int newsid){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("newsid",newsid);
        News news = newsDAO.selectOne(queryWrapper);
        return news;
    }

    //存浏览记录到数据库
    public void saveRecord(int userid,int newsid){
        QueryWrapper queryWrapper = new QueryWrapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String time = df.format(new Date());
//        System.out.println(time);
        queryWrapper.eq("userid",userid);
        queryWrapper.eq("newsid",newsid);

        Record r = recordDAO.selectOne(queryWrapper);
        if(r != null){
            //已经有记录，更新时间
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("userid",userid);
            wrapper.eq("newsid",newsid);
            Record record = new Record();
            record.setUserid(userid);
            record.setNewsid(newsid);
            record.setTime(time);
            int n = recordDAO.update(record,wrapper);
            if(n<=0){
                System.out.println("更新失败");
            }
        }else{
            Record record = new Record();
            record.setUserid(userid);
            record.setNewsid(newsid);
            record.setTime(time);
            int n = recordDAO.insert(record);
            if(n<=0){
                System.out.println("记录失败");
            }
        }
    }
}
