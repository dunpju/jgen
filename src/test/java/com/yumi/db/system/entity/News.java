package com.yumi.db.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author ffff
 * @since 2023-06-08
 */
@TableName("ts_news")
@ApiModel(value = "News对象", description = "")
public class News implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "news_id", type = IdType.AUTO)
    private Integer newsId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("点击量")
    private Integer clicknum;

    @ApiModelProperty("创建时间")
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

    @Override
    public String toString() {
        return "News{" +
            "newsId = " + newsId +
            ", title = " + title +
            ", clicknum = " + clicknum +
            ", createTime = " + createTime +
        "}";
    }
}
