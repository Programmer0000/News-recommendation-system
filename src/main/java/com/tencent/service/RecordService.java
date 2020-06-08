package com.tencent.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.dao.NewsDAO;
import com.tencent.dao.RecordDAO;
import com.tencent.pojo.News;
import com.tencent.pojo.Record;
import com.tencent.pojo.User;
import com.tencent.util.XTGL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RecordService {
    @Autowired
    private RecordDAO recordDAO;
    @Autowired
    private XTGL tj;
    @Autowired
    private NewsDAO newsDAO;

    public List xtgl(int userid){
        //最大相似度
        double max = 0;
        //最相似用户id
        int uid = 0;
        //存放随机数
        List lnum = new ArrayList();
        //存放推荐的新闻
        List<News> tjnews = new ArrayList();
        //存放不同用户与当前用户的相似度
        Map<Integer, Double> map = new HashMap();
        //取当前时间前两天数据
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String s = df.format(new Date());
        int t = Integer.parseInt(s);
        String time = String.valueOf(t-2);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid",userid);
//        queryWrapper.ge("time",time);
        //取当前用户浏览条数
        int u = recordDAO.selectCount(queryWrapper);
        if(u == 0){
            //没有浏览记录,新用户
//            return null;
        }else{
            List list = recordDAO.selectList(queryWrapper);
            for (int i=0;i<list.size();i++){
                //取该用户浏览的每一个news
                Record record = (Record) list.get(i);
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("newsid",record.getNewsid());
//                queryWrapper1.ge("time",time);
                //查询每一个news的浏览用户，倒排表
                List list1 = recordDAO.selectList(queryWrapper1);
                for (int j=0;j<list1.size();j++){
                    //取浏览过相同内容的用户
                    Record record1 = (Record) list1.get(j);
                    //对不是当前用户的用户操作,求总浏览条数
                    if(record1.getUserid() != userid){
                        QueryWrapper queryWrapper2 = new QueryWrapper();
                        queryWrapper2.eq("userid",record1.getUserid());
//                        queryWrapper2.ge("time",time);
                        int v = recordDAO.selectCount(queryWrapper2);
                        double sim = tj.xtgl(u,v);
                        if(map.get(record1.getUserid())==null){
                            map.put(record1.getUserid(),sim);
                        }
                        max = Math.max(max,sim);
                    }
                }
            }
            //遍历map，取出与max相同的userid
            for (Map.Entry m:map.entrySet()) {
                if(m.getValue().equals(max)){
                    uid = (int) m.getKey();
                }
            }
//            System.out.println(uid);
        }
        if(uid != 0){
            //已登录老用户
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("userid",uid);
            wrapper.ge("time",time);
            //查询近两天相似用户浏览记录
            List list = recordDAO.selectList(wrapper);
            if(list.isEmpty()){
                //相似用户最近无浏览记录
                return tjnews;
            }else if(list.size()<=11){
                //如果长度小于等于11
                int n = 11-list.size();
                for(int k=0;k<list.size();k++){
                    Record record = (Record) list.get(k);
                    QueryWrapper wrapper1 = new QueryWrapper();
                    wrapper1.eq("newsid",record.getNewsid());
                    News news = newsDAO.selectOne(wrapper1);
                    tjnews.add(news);
                    lnum.add(record.getNewsid());
                }
                if(n>0){
                    //相似用户浏览数不足11
                    QueryWrapper wrapper2 = new QueryWrapper();
                    //求出新闻总条数
                    int count = newsDAO.selectCount(wrapper2);
                    for(int v=0;v<n;v++){
                        int num = 0;
                        do {
                            num = (int) (Math.random()*count+1);
                        }while (tj.isRepeat(lnum,num));
                        lnum.add(num);
                        QueryWrapper wrapper3 = new QueryWrapper();
                        wrapper3.eq("newsid",num);
                        News news = newsDAO.selectOne(wrapper3);
                        tjnews.add(news);
                    }
                }
            }else{
                //如果长度大于11
                for(int k=0;k<11;k++){
                    int num = 0;
                    do {
                        num = (int) (Math.random()*list.size());
                    }while (tj.isRepeat(lnum,num));
                    lnum.add(num);
                    Record record = (Record) list.get(num);
                    QueryWrapper wrapper1 = new QueryWrapper();
                    wrapper1.eq("newsid",record.getNewsid());
                    News news = newsDAO.selectOne(wrapper1);
                    tjnews.add(news);
                }
            }
            return tjnews;
        }else{
            //新用户或未登录
            QueryWrapper wrapper = new QueryWrapper();
            //求出新闻总条数
            int count = newsDAO.selectCount(wrapper);
            for(int k=0;k<11;k++){
                int num = 0;
                do {
                    num = (int) (Math.random()*count+1);
                }while (tj.isRepeat(lnum,num));
                lnum.add(num);
                QueryWrapper wrapper1 = new QueryWrapper();
                wrapper1.eq("newsid",num);
                News news = newsDAO.selectOne(wrapper1);
                tjnews.add(news);
            }
            return tjnews;
        }
    }

    //未登录用户新闻首页推荐
    public List noLoginTJ(){
        //存放随机数
        List lnum = new ArrayList();
        //存放推荐的新闻
        List<News> tjnews = new ArrayList();
        QueryWrapper wrapper = new QueryWrapper();
        //求出新闻总条数
        int count = newsDAO.selectCount(wrapper);
        for(int k=0;k<11;k++){
            int num = 0;
            do {
                num = (int) (Math.random()*count+1);
            }while (tj.isRepeat(lnum,num));
            lnum.add(num);
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("newsid",num);
            News news = newsDAO.selectOne(wrapper1);
            tjnews.add(news);
        }
        return tjnews;
    }

    //热搜算法
    public List<News> rs(){
        //存放随机数
        List lnum = new ArrayList();
        //存放每个newsid的浏览条目数
        Map<Integer,Integer> map = new HashMap();
        //存放返回热搜集合
        List rs = new ArrayList();
        //排序集合
        List mp = new ArrayList();
        //存放不重复newsid
        List rnum = new ArrayList();
        //存放热搜newsid
        int nid = 0;
        //取当前时间前两天数据
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String s = df.format(new Date());
        int t = Integer.parseInt(s);
        String time = String.valueOf(t-2);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ge("time",time);
        List list = recordDAO.selectList(queryWrapper);
        if(list.size()<=4){
            //如果近两天没有用户使用系统浏览或者浏览记录小于4，忽略不计
            QueryWrapper wrapper = new QueryWrapper();
            //求出新闻总条数
            int count = newsDAO.selectCount(wrapper);
            for(int k=0;k<4;k++){
                int num = 0;
                do {
                    num = (int) (Math.random()*count+1);
                }while (tj.isRepeat(lnum,num));
                lnum.add(num);
                QueryWrapper wrapper1 = new QueryWrapper();
                wrapper1.eq("newsid",num);
                News news = newsDAO.selectOne(wrapper1);
                rs.add(news);
            }
            return rs;

        }else{
            //近两天用户浏览数目大于4
            //循环遍历近两天的浏览记录
            for(int i=0;i<list.size();i++){
                Record record = (Record) list.get(i);
                if(tj.isRepeat(rnum,record.getNewsid())){
                    continue;
                }
                rnum.add(record.getNewsid());
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("newsid",record.getNewsid());
                wrapper.ge("time",time);
                int count = recordDAO.selectCount(wrapper);
                //放入数组准备排序
                mp.add(count);
                //记录news的条数
                map.put(record.getNewsid(),count);
            }
            //冒泡排序，从大到小,先转换为数组
            Integer[] mpArray = new Integer[mp.size()];
            for(int i=0;i<mp.size();i++){
                mpArray[i] = (Integer) mp.get(i);
            }
            //排序,从大到小
            for(int j=0;j<mpArray.length;j++){
                for(int k=0;k<mpArray.length-j-1;k++){
                    int temp = 0;
                    if(mpArray[k]<mpArray[k+1]){
                        temp = mpArray[k];
                        mpArray[k] = mpArray[k+1];
                        mpArray[k+1] = temp;
                    }
                }
            }
            //遍历排序好的数组前四个
            for(int v=0;v<4;v++){
                for (Map.Entry m:map.entrySet()) {
//                    System.out.println("id="+m.getKey());
                    if(m.getValue().equals(mpArray[v])){
                        nid = (int) m.getKey();
//                        System.out.println(nid);
                        QueryWrapper wrapper1 = new QueryWrapper();
                        wrapper1.eq("newsid",m.getKey());
                        News news = newsDAO.selectOne(wrapper1);
                        rs.add(news);
                        break;
                    }
                }
                map.remove(nid);
            }
            return rs;
        }
    }
}
