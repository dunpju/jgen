package com.dunpju.orm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BaseModel<T> extends BaseMapper<T> {
    @Select("${_sql_}")
    public Map<String, Object> first(Map<String, Object> map);
    @Select("${_sql_}")
    public List<Map<String, Object>> get(Map<String, Object> map);
}
