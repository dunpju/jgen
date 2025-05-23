package io.dunpju.orm;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface IMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    @Select("${_sql_}")
    Map<String, Object> execute(Map<String, Object> map);
    @Select("${_sql_}")
    Map<String, Object> query(Map<String, Object> map);
    @Select("${_sql_}")
    List<Map<String, Object>> count(Map<String, Object> map);
    @Select("${_sql_}")
    Map<String, Object> first(Map<String, Object> map);
    @Select("${_sql_}")
    List<Map<String, Object>> get(Map<String, Object> map);
    /**
     * 插入或更具主键更新所有数据
     */
    Integer insertOrUnionKeyUpdate(List<T> list);
}
