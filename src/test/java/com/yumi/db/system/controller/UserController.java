package com.yumi.db.system.controller;

import com.yumi.db.system.dao.UserDao;
import com.yumi.db.system.entity.User;
import com.yumi.db.system.service.IUserService;
import com.yumi.db.system.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ffff
 * @since 2023-06-08
 */
@Controller
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    UserDao userDao;

    @GetMapping("test")
    public User test() {
        userService.getById();
        return userDao.getById();
    }

}
