package com.yumi.db.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dunpju.annotations.Message;
import com.dunpju.orm.BaseField;
import com.dunpju.orm.BaseModel;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("ts_news")
@Message(value = "")
@Data
public class News extends BaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    public enum FIELD implements BaseField {
        news_id,
        title,
        clicknum,
        create_time;
    }

    @Message("主键")
    @TableId(value = "news_id", type = IdType.AUTO)
    private Integer newsId;
    @Message("标题")
    private String title;
    @Message("点击量")
    private BigDecimal clicknum;
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