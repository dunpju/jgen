package com.yumi.db.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dunpju.annotations.Message;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("ts_news")
@Message(value = "")
@Data
public class News implements Serializable {

    public enum FIELD implements Serializable {
        news_id,
        title,
        clicknum,
        create_time;

        public String As(String alias) {
            return String.format("%s AS %s", this, alias);
        }
    }

    @Message("主键")
    @TableId(value = "news_id", type = IdType.AUTO)
    private Integer newsId;
    @Message("标题")
    private String title;
    @Message("点击量")
    private Integer clicknum;
    @Message("创建时间")
    private LocalDateTime createTime;

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