package com.tencent.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.dao.CollectionDAO;
import com.tencent.dao.NewsDAO;
import com.tencent.pojo.Collection;
import com.tencent.pojo.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionService {
    @Autowired
    private CollectionDAO collectionDAO;
    @Autowired
    private NewsDAO newsDAO;

    //判断是否已经收藏
    public boolean existCollection(int userid,int newsid){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        queryWrapper.eq("newsid",newsid);

        Integer count = collectionDAO.selectCount(queryWrapper);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    //添加收藏
    public int addCollection(int userid,int newsid){
        Collection collection = new Collection();
        collection.setUserid(userid);
        collection.setNewsid(newsid);

        int n = collectionDAO.insert(collection);
        return n;
    }

    //根据userid查询收藏
    public List queryCollectionByUserId(int userid){
        //存放返回收藏数据
        List sc = new ArrayList();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        List list = collectionDAO.selectList(queryWrapper);

        for(int i=0;i<list.size();i++){
            Collection collection = (Collection) list.get(i);
            //新建一个news对象,存放userid和title
            News news = new News();
            news.setNewsid(collection.getNewsid());

            //根据newsid查title
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("newsid",collection.getNewsid());
            News n = newsDAO.selectOne(wrapper);

            news.setTitle(n.getTitle());
            sc.add(news);
        }
        return sc;
    }

    //删除收藏
    public int deleteCollection(int userid,int newsid){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
        queryWrapper.eq("newsid",newsid);

        int n = collectionDAO.delete(queryWrapper);
        return n;
    }
}
