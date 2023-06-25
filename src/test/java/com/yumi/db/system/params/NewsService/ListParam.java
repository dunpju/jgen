package com.yumi.db.system.params.NewsService;

import lombok.Data;

@Data
public class ListParam {
    private String keyword;
    private long page;
    private long pageSize;
}
