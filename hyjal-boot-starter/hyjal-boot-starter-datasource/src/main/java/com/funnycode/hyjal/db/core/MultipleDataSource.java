package com.funnycode.hyjal.db.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 基于Spring-jdbc的多数剧源实现
 *
 * @author tc
 * @date 2019-03-21
 */
public class MultipleDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

    public static void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }

}
