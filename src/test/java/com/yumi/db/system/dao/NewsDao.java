package com.yumi.db.system.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dunpju.orm.Builder;
import com.dunpju.orm.Paged;
import com.yumi.db.system.entity.NewsEntity;
import com.yumi.db.system.mapper.NewsMapper;
import com.yumi.db.system.mapper.UserMapper;
import com.yumi.db.system.model.News;
import com.yumi.db.system.model.User;
import com.yumi.db.system.vo.NewSVo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Scope("prototype")
public class NewsDao extends ServiceImpl<NewsMapper, News> {

    News model = new News();
    private Builder<NewsMapper, News> builder;

    public Builder<NewsMapper, News> model() {
        if (null == builder) {
            builder = new Builder<>(this.baseMapper, this.model).FROM(getEntityClass());
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
//                this.model.setClicknum("clicknum + 10");
//                Wrapper<News> updateWrapper = new <> ();
//                updateWrapper.e
//                this.baseMapper.update()
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
    public int Add() {
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

    public News getByNewsId(Long newsId) {
        return this.model().SELECT("*").WHERE(News.FIELD.news_id, "=", newsId).first(News.class);
    }

    public List<News> getByNewsIds(List<Object> newsIds) {
        return this.model().SELECT("*").WHERE_IN(News.FIELD.news_id, newsIds).get(News.class);
    }

    public int deleteByNewsId(Integer newsId) {
        return this.baseMapper.deleteById(newsId);
    }

    public BigDecimal sumClickNumByNewsIds(List<Object> newsIds) {
        return this.model().WHERE_IN(News.FIELD.news_id, newsIds).sum(News.FIELD.clicknum);
    }

    public Long countByNewsIds(List<Object> newsIds) {
        return this.model().WHERE_IN(News.FIELD.news_id, newsIds).count();
    }

    public Paged<NewSVo> getList() {
        Builder<UserMapper, User> b = new UserDao().model();
        return this.model().AS("a").SELECT(
                        News.FIELD.news_id,
                        News.FIELD.title,
                        News.FIELD.clicknum
                ).INNER_JOIN(b.AS("b").ON(User.FIELD.id.Pre("b"), "=", News.FIELD.news_id.Pre("a"),
                        "AND",
                        News.FIELD.clicknum.Pre("a"), ">", "1")).
                GROUP_BY(News.FIELD.title).
                ORDER_BY(News.FIELD.news_id.DESC()).
                paginate(1, 2, NewSVo.class);
    }

    public News getById() {
        Builder<NewsMapper, News> query = new Builder<>(this.baseMapper, this.model);

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
