package io.dunpju.orm;

import java.util.List;

/**
 * @Author liuguoqiang
 * @Date 2025/4/27
 * @Version 1.0
 */
public interface IService<T> extends com.baomidou.mybatisplus.extension.service.IService<T> {

    /**
     * 插入后根据主键更新所有字段
     */
    Integer insertOrUnionKeyUpdate(List<T> list, Integer batchSize);

    /**
     * 插入后根据主键更新所有字段
     */
    Integer insertOrUnionKeyUpdate(List<T> list);
}
