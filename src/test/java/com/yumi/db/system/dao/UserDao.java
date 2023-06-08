package com.yumi.db.system.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dunpju.orm.Builder;
import com.yumi.db.system.entity.User;
import com.yumi.db.system.mapper.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends ServiceImpl<UserMapper, User> {
    public User getById() {
        Builder query = new Builder();

        query.SELECT("id", "uname", "u_tel", "score");
        query.FROM(getEntityClass().getAnnotation(TableName.class).value());
        query.WHERE("id", "=", String.valueOf(1001));

        User user = this.baseMapper.first(query.map());
        System.out.println(user);
        return user;
    }
}
