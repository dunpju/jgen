package com.yumi.db.system.mapper;

import io.dunpju.orm.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.yumi.db.system.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}