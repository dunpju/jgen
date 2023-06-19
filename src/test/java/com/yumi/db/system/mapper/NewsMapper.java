package com.yumi.db.system.mapper;

import com.dunpju.orm.BaseMapper;
import com.yumi.db.system.model.News;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ffff
 * @since 2023-06-08
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
