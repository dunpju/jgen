package com.yumi.db.system.service.impl;

import com.yumi.db.system.entity.News;
import com.yumi.db.system.mapper.NewsMapper;
import com.yumi.db.system.service.INewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ffff
 * @since 2023-06-08
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

}
