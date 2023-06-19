package com.dunpju.orm;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    @Select("${_sql_}")
    public Map<String, Object> execute(Map<String, Object> map);
    @Select("${_sql_}")
    public Map<String, Object> query(Map<String, Object> map);
    @Select("${_sql_}")
    public List<Map<String, Object>> count(Map<String, Object> map);
    @Select("${_sql_}")
    public Map<String, Object> first(Map<String, Object> map);
    @Select("${_sql_}")
    public List<Map<String, Object>> get(Map<String, Object> map);
}
