package com.yumi.db.system.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dunpju.orm.Builder;
import io.dunpju.orm.Paged;
import com.yumi.db.system.mapper.NewsMapper;
import com.yumi.db.system.model.News;
import com.yumi.db.system.vo.NewSVo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Scope("prototype")
public class NewsDao1 extends ServiceImpl<NewsMapper, News> {

    News model = new News();
    private Builder<NewsMapper, News> builder;

    public Builder<NewsMapper, News> model() {
        if (null == builder) {
            builder = new Builder<>(this.baseMapper, this.model).FROM(getEntityClass());
        }
        return builder;
    }

    /*public void setData(NewsEntity newsEntity) {
        IFlag flag = newsEntity.Flag();
        if (null != flag) { // 编辑
            model = this.model().SELECT("*").WHERE(News.FIELD.news_id, "=", newsEntity.getNewsId()).first(News.class);
            if (null == model || model.getNewsId() == null) {
                throw new RuntimeException("News数据不存在");
            }
            if (NewsEntity.FLAG.Delete == flag) {
                // TODO::映射入库字段
            } else if (NewsEntity.FLAG.Update == flag) {
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
    }*/

    /**
     * 插入
     *
     * @return Integer
     */
    public long Add() {
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

    public News getByNewsId(int newsId) {
        return this.model().SELECT("*").WHERE(News.FIELD.news_id, "=", newsId).first(News.class);
    }

    public List<News> getByNewsIds(List<Object> newsIds) {
        return this.model().SELECT("*").WHERE_IN(News.FIELD.news_id, newsIds).get(News.class);
    }

    public int deleteByNewsId(int newsId) {
        return this.baseMapper.deleteById(newsId);
    }

    public BigDecimal sumClickNumByNewsIds(List<Object> newsIds) {
        return this.model().WHERE_IN(News.FIELD.news_id, newsIds).sum(News.FIELD.clicknum);
    }

    public Long countByNewsIds(List<Object> newsIds) {
        return this.model().WHERE_IN(News.FIELD.news_id, newsIds).count();
    }

    public Paged<NewSVo> getList(long page, long pageSize) {
        return this.model().paginate(page, pageSize, NewSVo.class);
    }
}
