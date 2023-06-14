package com.yumi.db.system.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dunpju.orm.Builder;
import com.yumi.db.system.entity.NewsEntity;
import com.yumi.db.system.mapper.NewsMapper;
import com.yumi.db.system.model.News;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class NewsDao extends ServiceImpl<NewsMapper, News> {

    News news = new News();

    public Builder<NewsMapper, News> model() {
         return new Builder<>(this.baseMapper).FROM(getEntityClass());
    }

    public void setData(NewsEntity newsEntity) {
        NewsEntity.Flag flag = newsEntity.currentFlag();
        if (null != flag) { // 编辑
            news = this.model().SELECT("*").WHERE(News.FIELD.news_id, "=", newsEntity.getNewsId()).first();
            if (null == news) {
                throw new RuntimeException("News数据不存在");
            }
            if (NewsEntity.Flag.Delete == flag) {
                // TODO::映射入库字段
            } else if (NewsEntity.Flag.Update == flag) {
                // TODO::映射入库字段
                this.news.setTitle(newsEntity.getTitle());
            }
        } else { // 新增
            //TODO::修改入库字段
            this.news.setTitle(newsEntity.getTitle());
            this.news.setCreateTime(newsEntity.getCreateTime());
        }
    }

    /**
     * 插入
     *
     * @return Integer
     */
    public Integer Add() {
        this.baseMapper.insert(this.news);
        return this.news.getNewsId();
    }

    /**
     * 修改
     *
     * @return int
     */
    public int Update() {
        return this.baseMapper.updateById(this.news);
    }

    public News getByNewsId(Integer newsId) {
        return this.model().SELECT("*").WHERE(News.FIELD.news_id, "=", newsId).first();
//        return this.baseMapper.first(this.query.toSql());
    }


    public News getById() {
        Builder<NewsMapper, News> query = new Builder<>(this.baseMapper);

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
