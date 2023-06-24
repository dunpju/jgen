package com.yumi.db.system.controller;

import com.yumi.db.system.dao.NewsDao1;
import com.yumi.db.system.dao.UserDao;
import com.yumi.db.system.entity.NewsEntity;
import com.yumi.db.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    UserDao userDao;

    NewsDao1 newsDao1;
    @Autowired
    ApplicationContext applicationContext;

    @GetMapping("/test")
    public User test() {
//        userService.getById();
        NewsDao1 newsDao1 = applicationContext.getBean(NewsDao1.class);

        System.out.println(newsDao1);

        NewsEntity newsEntity = new NewsEntity();
        // 测试插入
        newsEntity.setTitle("ggghh");
        newsDao1.setData(newsEntity);
        int id = Math.toIntExact(newsDao1.Add());
        System.out.println(id);

        // 测试查询
        System.out.println(newsDao1.getByNewsId(id));

        // 测试修改
        newsEntity.SetFlag(NewsEntity.FLAG.Update);
        newsEntity.setNewsId((long) id);
        newsEntity.setTitle("ggghh111");
        newsDao1.setData(newsEntity);
        newsDao1.Update();
        System.out.println(newsDao1.getByNewsId(id));
        // 测试批量查询
        List<Object> newsIds = new ArrayList<>();
        newsIds.add(1);
        newsIds.add(2);
        System.out.println(newsDao1.getByNewsIds(newsIds));
        System.out.println(newsDao1.sumClickNumByNewsIds(newsIds));
        System.out.println(newsDao1.countByNewsIds(newsIds));
        System.out.println(newsDao1.getList(1, 10));
        return null;
    }

}
