package com.yumi.db.system.entity;

import com.dunpju.annotations.Message;
import lombok.NonNull;

import java.time.LocalDateTime;

@Message(value = "News实体")
public class NewsEntity {

    public enum Flag {
        Delete,
        Update
    }

    private Flag currentFlag;

    public Flag currentFlag() {
        Flag flag = currentFlag;
        currentFlag = null;
        return flag;
    }

    public void setFlag(Flag currentFlag) {
        this.currentFlag = currentFlag;
    }

    @Message("主键")
    private Integer newsId;

    @Message("标题")
    private String title;

    @Message("点击量")
    private Integer clicknum;

    @Message("创建时间")
    private LocalDateTime createTime;

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
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
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
