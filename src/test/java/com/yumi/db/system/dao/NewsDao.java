package com.yumi.db.system.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dunpju.orm.Builder;
import com.yumi.db.system.entity.NewsEntity;
import com.yumi.db.system.mapper.NewsMapper;
import com.yumi.db.system.model.News;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Scope("prototype")
public class NewsDao extends ServiceImpl<NewsMapper, News> {

    News model = new News();
    private Builder<NewsMapper, News> builder;

    public Builder<NewsMapper, News> model() {
        if (null == builder) {
            builder = new Builder<>(this.baseMapper).FROM(getEntityClass());
        }
        return builder;
    }

    public void setData(NewsEntity newsEntity) {
        NewsEntity.Flag flag = newsEntity.currentFlag();
        if (null != flag) { // 编辑
            model = this.model().SELECT("*").WHERE(News.FIELD.news_id, "=", newsEntity.getNewsId()).first(News.class);
            if (null == model || model.getNewsId() == null) {
                throw new RuntimeException("News数据不存在");
            }
            if (NewsEntity.Flag.Delete == flag) {
                // TODO::映射入库字段
            } else if (NewsEntity.Flag.Update == flag) {
                // TODO::映射入库字段
                this.model.setTitle(newsEntity.getTitle());
                this.model.setClicknum(newsEntity.getTitle());
            }
        } else { // 新增
            //TODO::修改入库字段
            this.model.setTitle(newsEntity.getTitle());
            this.model.setCreateTime(newsEntity.getCreateTime());
        }
    }

    /**
     * 插入
     *
     * @return Integer
     */
    public Integer Add() {
        this.baseMapper.insert(this.model);
        return this.model.getNewsId();
    }

    /**
     * 修改
     *
     * @return int
     */
    public int Update() {
        return this.baseMapper.updateById(this.model);
    }

    public News getByNewsId(Integer newsId) {
        return this.model().SELECT("*").WHERE(News.FIELD.news_id, "=", newsId).first(News.class);
    }

    public List<News> getByNewsIds(List<Object> newsIds) {
        return this.model().SELECT("*").WHERE_IN(News.FIELD.news_id, newsIds).get(News.class);
    }

    public Integer sumClickNumByNewsIds(List<Object> newsIds) {
        this.baseMapper.selectCount()
        return null;
    }

    public int deleteByNewsId(Integer newsId) {
        return this.baseMapper.deleteById(newsId);
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
