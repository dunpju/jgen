package com.yumi.db.system.entity;

import com.dunpju.annotations.Message;
import com.dunpju.entity.BaseEntity;
import com.dunpju.entity.IFlag;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Message(value = "新闻表")
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

}
