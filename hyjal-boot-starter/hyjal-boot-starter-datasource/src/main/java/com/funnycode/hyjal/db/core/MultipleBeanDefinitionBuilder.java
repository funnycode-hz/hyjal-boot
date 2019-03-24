package com.funnycode.hyjal.db.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

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

    private Map<Object, Object> targetDataSources;

    private String defaultTargetDataSource;

    public MultipleBeanDefinitionBuilder(ApplicationContext context) {
        this.context = context;
    }

    public synchronized MultipleBeanDefinitionBuilder defaultTargetDataSource(String defaultTargetDataSource) {
        this.defaultTargetDataSource = this.defaultTargetDataSource == null ? defaultTargetDataSource
            : this.defaultTargetDataSource;
        return this;
    }

    public synchronized MultipleBeanDefinitionBuilder targetDataSources(String name, BeanDefinition beanDefinition) {
        if (CollectionUtils.isEmpty(targetDataSources)) {
            targetDataSources = new HashMap<>();
        }

        targetDataSources.putIfAbsent(name, beanDefinition);
        return this;
    }

    public synchronized MultipleBeanDefinitionBuilder targetDataSources(Map<Object, Object> targetDataSources) {
        if (CollectionUtils.isEmpty(targetDataSources)) {
            this.targetDataSources = targetDataSources;
        } else {
            this.targetDataSources.putAll(targetDataSources);
        }

        return this;
    }

    BeanDefinition build() {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MultipleDataSource.class);
        builder.addPropertyReference("defaultTargetDataSource", defaultTargetDataSource);
        builder.addPropertyValue("targetDataSources", targetDataSources);

        return builder.getBeanDefinition();
    }

}


