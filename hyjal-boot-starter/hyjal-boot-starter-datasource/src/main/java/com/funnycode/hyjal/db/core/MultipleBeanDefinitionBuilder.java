package com.funnycode.hyjal.db.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源对象构建器
 *
 * @author tc
 * @date 2019-03-21
 */
public class MultipleBeanDefinitionBuilder {

    private static final Logger logger = LoggerFactory.getLogger(MultipleBeanDefinitionBuilder.class);

    private ApplicationContext context;

    private Map<String, DataSource> extendDataSources = new HashMap<>();

    private DataSource defaultTargetDataSource;

    public MultipleBeanDefinitionBuilder(ApplicationContext context) {
        this.context = context;
    }

    public synchronized MultipleBeanDefinitionBuilder defaultTargetDataSource(DataSource defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
        return this;
    }

    public synchronized MultipleBeanDefinitionBuilder targetDataSources(String key, DataSource dataSource) {
        extendDataSources.putIfAbsent(key, dataSource);
        return this;
    }

    public synchronized MultipleBeanDefinitionBuilder targetDataSources(Map<String, DataSource> extendDataSources) {
        this.extendDataSources.putAll(extendDataSources);
        return this;
    }

    BeanDefinition build() {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MultipleDataSource.class);

        GenericBeanDefinition beanDefinition = (GenericBeanDefinition)builder.getBeanDefinition();
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("defaultTargetDataSource", defaultTargetDataSource);
        mpv.add("targetDataSources", extendDataSources);
        beanDefinition.setPropertyValues(mpv);
        return beanDefinition;
    }

}


