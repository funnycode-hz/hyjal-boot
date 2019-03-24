package com.funnycode.hyjal.db.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tc
 * @date 2019-03-24
 */
public class MultipleDataSourceContextHolder {

    private static Logger logger = LoggerFactory.getLogger(MultipleDataSourceContextHolder.class);

    /**
     * 存储已经注册的数据源的key,注意，默认数据源的key不用存储
     */
    public static List<String> dataSourceIds = new ArrayList<>();

    /**
     * 当前持有的key
     */
    private static final ThreadLocal<String> HOLDER = new InheritableThreadLocal<String>();

    public static String getDataSourceRouterKey() {
        return HOLDER.get();
    }

    public static void setDataSourceRouterKey(String dataSourceRouterKey) {
        logger.info("切换至 {} 数据源 ", dataSourceRouterKey);
        HOLDER.set(dataSourceRouterKey);
    }

    /**
     * 设置数据源之前一定要先移除
     */
    public static void removeDataSourceRouterKey() {
        HOLDER.remove();
    }

    /**
     * 判断指定DataSource当前是否存在
     *
     * @param dataSourceId
     * @return
     */
    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }

}
