package com.tencent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.pojo.News;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface NewsDAO extends BaseMapper<News> {
}
