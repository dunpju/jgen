package com.yumi.db.system.params.NewsService;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ListParam {
    private String keyword;
    private long page;
    private long pageSize;
}
