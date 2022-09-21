package com.example.spatialoperation.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("动态获取数据源——{}",DataSourceUtil.getDB());
        return DataSourceUtil.getDB();
    }
}
