package com.dunpju.stubs;

public class DaoStub {

    private final StringBuffer property = new StringBuffer();

    public String stub() {
        String tpl = """
                package com.yumi.db.system.dao;
                                
                import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
                import com.dunpju.entity.IFlag;
                import com.dunpju.orm.Builder;
                import com.dunpju.orm.Paged;
                import com.yumi.db.system.entity.NewsEntity;
                import com.yumi.db.system.mapper.NewsMapper;
                import com.yumi.db.system.model.News;
                import com.yumi.db.system.vo.NewSVo;
                import org.springframework.context.annotation.Scope;
                import org.springframework.stereotype.Repository;
                                
                import java.math.BigDecimal;
                import java.util.List;
                                
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
                            }
                        } else { // 新增
                            //TODO::修改入库字段
                            %Create_%
                            this.model.setTitle(newsEntity.getTitle());
                            this.model.setCreateTime(newsEntity.getCreateTime());
                        }
                    }
                                
                    public Long Add() {
                        this.baseMapper.insert(this.model);
                        return this.model.getNewsId();
                    }
                                
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
                """;
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        return tpl;
    }
}
