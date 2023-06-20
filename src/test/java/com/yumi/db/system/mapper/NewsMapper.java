package com.yumi.db.system.mapper;

import com.dunpju.orm.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.yumi.db.system.model.News;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
}