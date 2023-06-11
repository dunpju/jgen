package com.yumi.db.system.service.impl;

import com.yumi.db.system.entity.Users;
import com.yumi.db.system.mapper.UsersMapper;
import com.yumi.db.system.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ffff
 * @since 2023-06-10
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
