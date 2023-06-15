package com.yumi.db.system.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yumi.db.system.mapper.UserMapper;
import com.yumi.db.system.model.User;
import com.yumi.db.system.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ffff
 * @since 2023-06-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    public void getById() {
        /*this.baseMapper.selectOne(Wrappers.<User>query().lambda().
                eq(User::getId, 1));*/
        SQL sql = new SQL() {{
            SELECT("id,uname,u_tel, score");
            FROM(getEntityClass().getAnnotation(TableName.class).value());
            WHERE("id=#{id}");
            AND();
            WHERE("uname=#{uname}");
        }};
        System.out.println(sql);
/*        Map<String, Object> map = new HashMap<>();
        map.put("sql", sql.toString());
        map.put("id", 1002);
        map.put("uname", "dfg");
        User user = this.baseMapper.get(map);*/


//        Builder<UserMapper, User> query = new Builder<>(this.baseMapper);
//        query.SELECT("id", "uname", "u_tel", "score");
//        query.FROM(getEntityClass().getAnnotation(TableName.class).value());
//                WHERE("id", "=", String.valueOf(1002)).
//        query.LIKE("uname", "%d%");

//        List<User> user = this.baseMapper.get(query.toSql());
//        System.out.println(user);
    }

}
