package com.yumi.db.system.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "com.yumi.db.system.controller", useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM,
                classes = {com.yumi.db.system.filter.MtyTypeFilter.class}))
public class AppConfig {
}
