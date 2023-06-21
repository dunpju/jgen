package com.yumi.db.system.entity;

import com.dunpju.annotations.Message;
import com.dunpju.entity.BaseEntity;
import com.dunpju.entity.IFlag;

import java.time.LocalDateTime;

@Message(value = "News实体")
public class NewsEntity extends BaseEntity {

    public enum FLAG implements IFlag {
        Delete,
        Update;
    }

    @Message("主键")
    private Long newsId;

    @Message("标题")
    private String title;

    @Message("点击量")
    private Integer clicknum;

    @Message("创建时间")
    private LocalDateTime createTime;

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getClicknum() {
        return clicknum;
    }

    public void setClicknum(Integer clicknum) {
        this.clicknum = clicknum;
    }

    public LocalDateTime getCreateTime() {
        return createTime = LocalDateTime.now();
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
