package com.yumi.db.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yumi.db.system.model.News;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ffff
 * @since 2023-06-08
 */
public interface NewsMapper extends BaseMapper<News> {
    @Select("${_sql_}")
    public News first(Map<String, Object> map);
    @Select("${_sql_}")
    public List<News> get(Map<String, Object> map);
}
