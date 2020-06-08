package com.tencent.service;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.dao.UserDAO;
import com.tencent.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RedisTemplate redisTemplate;

    //注册
    public int regist(String username,String password,String mobile){
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("username",username);
        //MD5加密
        String pre = "12Dis!!#@,";
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String digestHex = md5.digestHex(password+pre);
        String pwd = md5.digestHex(pre+digestHex);

        User user = userDAO.selectOne(queryWrapper1);

        if(user != null){
            return 0;
        }else{
            User user1 = new User();
            user1.setUsername(username);
            user1.setPassword(pwd);
            user1.setTel(mobile);
            int n = userDAO.insert(user1);
            return n;
        }
    }
    //登录
    public User login(String username,String password){
        QueryWrapper queryWrapper = new QueryWrapper();
        String pre = "12Dis!!#@,";
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String digestHex = md5.digestHex(password+pre);
        String pwd = md5.digestHex(pre+digestHex);

        queryWrapper.eq("username",username);
        queryWrapper.eq("password",pwd);

        User user = userDAO.selectOne(queryWrapper);
        if(user!=null){
            return user;
        }else{
            return null;
        }
    }
}
