package com.yumi.db.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dunpju.orm.Paged;
import com.yumi.db.system.dao.NewsDao;
import com.yumi.db.system.entity.NewsEntity;
import com.yumi.db.system.mapper.NewsMapper;
import com.yumi.db.system.model.News;
import com.yumi.db.system.params.NewsService.AddParam;
import com.yumi.db.system.params.NewsService.EditParam;
import com.yumi.db.system.params.NewsService.ListParam;
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

    public Paged<NewsVO> getList(ListParam params) {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        return newsDao.getList(params.getPage(), params.getPageSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(AddParam params) {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        NewsEntity newsEntity = new NewsEntity();
        // TODO::填充业务
        newsDao.setData(newsEntity);
        newsDao.Add();
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(EditParam params) {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.SetFlag(NewsEntity.FLAG.Update);
        // TODO::填充业务
        newsDao.setData(newsEntity);
        newsDao.Add();
    }

    public News details(Integer newsId) {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        return newsDao.getByNewsId(newsId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer newsId) {
        NewsDao newsDao = applicationContext.getBean(NewsDao.class);
        // TODO::填充业务
    }
}
