package com.yumi.db.system.controller;

import com.yumi.db.system.params.NewsService.ListParam;
import com.yumi.db.system.service.impl.NewsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system/news")
public class NewsController {
    @Autowired
    NewsServiceImpl newsServiceImpl;
    @GetMapping("/list")
    public void list(ListParam params) {
        newsServiceImpl.getList(params);
    }
}
