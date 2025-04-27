package io.dunpju.sqlMethod;

import io.dunpju.orm.IMapper;

import java.util.List;

public interface MyBaseMapper<T> extends IMapper<T> {

    /**
     * 插入后根据主键更新所有字段
     */
    Integer insertOrUnionKeyUpdate(List<T> list);
}