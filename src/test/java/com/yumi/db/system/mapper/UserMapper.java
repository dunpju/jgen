package com.yumi.db.system.mapper;

import com.yumi.db.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface UserMapper extends BaseMapper<User> {

    @Select("${sql}")
    public User first(Map<String, Object> map);
    @Select("${sql}")
    public List<User> get(Map<String, Object> map);

}
