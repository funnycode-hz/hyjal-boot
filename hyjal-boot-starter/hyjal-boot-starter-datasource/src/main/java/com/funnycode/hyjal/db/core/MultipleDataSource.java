package com.funnycode.hyjal.db.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 基于Spring-jdbc的多数剧源实现
 *
 * @author tc
 * @date 2019-03-21
 */
public class MultipleDataSource extends AbstractRoutingDataSource {

    private static Logger logger = LoggerFactory.getLogger(MultipleDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceRouterKey = MultipleDataSourceContextHolder.getDataSourceRouterKey();
        logger.info("当前数据源是：{} ", dataSourceRouterKey);
        return MultipleDataSourceContextHolder.getDataSourceRouterKey();
    }

}
