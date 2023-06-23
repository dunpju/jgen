package com.yumi.db.system.controller;

import com.yumi.db.system.dao.NewsDao;
import com.yumi.db.system.dao.UserDao;
import com.yumi.db.system.entity.NewsEntity;
import com.yumi.db.system.entity.User;
import com.yumi.db.system.service.IUserService;
import com.yumi.db.system.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
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

    NewsDao newsDao;
    @Autowired
    ApplicationContext applicationContext;

    @GetMapping("/test")
    public User test() {
//        userService.getById();
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);

        System.out.println(newsDao);

        NewsEntity newsEntity = new NewsEntity();
        // 测试插入
        newsEntity.setTitle("ggghh");
        newsDao.setData(newsEntity);
        int id = Math.toIntExact(newsDao.Add());
        System.out.println(id);

        // 测试查询
        System.out.println(newsDao.getByNewsId(id));

        // 测试修改
        newsEntity.SetFlag(NewsEntity.FLAG.Update);
        newsEntity.setNewsId((long) id);
        newsEntity.setTitle("ggghh111");
        newsDao.setData(newsEntity);
        newsDao.Update();
        System.out.println(newsDao.getByNewsId(id));
        // 测试批量查询
        List<Object> newsIds = new ArrayList<>();
        newsIds.add(1);
        newsIds.add(2);
        System.out.println(newsDao.getByNewsIds(newsIds));
        System.out.println(newsDao.sumClickNumByNewsIds(newsIds));
        System.out.println(newsDao.countByNewsIds(newsIds));
        System.out.println(newsDao.getList(1, 10));
        return null;
    }

}
