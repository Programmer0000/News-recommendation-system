package com.tencent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDAO extends BaseMapper<User> {
}
