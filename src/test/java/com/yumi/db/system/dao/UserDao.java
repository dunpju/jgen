package com.yumi.db.system.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dunpju.orm.Builder;
import com.yumi.db.system.mapper.UserMapper;
import com.yumi.db.system.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends ServiceImpl<UserMapper, User> {

    User model = new User();
    private Builder<UserMapper, User> builder;

    public  Builder<UserMapper, User> model() {
        if (null == builder) {
            builder = new Builder<>(this.baseMapper).FROM(getEntityClass());
        }
        return builder;
    }

    public User getById() {
        Builder<UserMapper, User> query = new Builder<>(this.baseMapper);

        /*query.SELECT("id", "uname", "u_tel", "score");
        query.FROM(getEntityClass());
        query.WHERE("id", "=", 1001);

        User user = this.baseMapper.first(query.map());
        System.out.println(user);

        query.SELECT("id", "uname");
        query.FROM(getEntityClass());
        System.out.println(this.baseMapper.get(query.map()));*/

        query.SELECT("id", "uname", "u_tel");
        query.FROM(getEntityClass());
        query.BETWEEN("id", 3, 6);
//        System.out.println(this.baseMapper.get(query.toSql()));

        return null;
    }
}
