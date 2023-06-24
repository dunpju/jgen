package com.yumi.db.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dunpju.orm.Paged;
import com.yumi.db.system.dao.NewsDao;
import com.yumi.db.system.entity.NewsEntity;
import com.yumi.db.system.mapper.NewsMapper;
import com.yumi.db.system.model.News;
import com.yumi.db.system.service.INewsService;
import com.yumi.db.system.vo.News.NewsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {
    @Autowired
    ApplicationContext applicationContext;

    public Paged<NewsVO> getList(long page, long pageSize) {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        return newsDao.getList(page, pageSize);
    }

    @Transactional
    public void add() {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        NewsEntity newsEntity = new NewsEntity();
        // TODO::填充业务
        newsDao.setData(newsEntity);
        newsDao.Add();
    }

    @Transactional
    public void edit() {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.SetFlag(NewsEntity.FLAG.Update);
        // TODO::填充业务
        newsDao.setData(newsEntity);
        newsDao.Add();
    }

    public News details() {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        return newsDao.getByNewsId(0L);
    }

    @Transactional
    public void delete() {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        // TODO::填充业务
    }
}
