package com.funnycode.hyjal.db.core;

import com.funnycode.hyjal.db.annotation.HyjalDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源自动组装触发类
 *
 * @author tc
 * @date 2019-03-21
 */
public class MultipleDataSourceProcess implements
    BeanClassLoaderAware, EnvironmentAware, BeanFactoryPostProcessor, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(MultipleDataSourceProcess.class);

    private ApplicationContext applicationContext;

    private ClassLoader classLoader;

    private Map<String, BeanDefinition> beanDefinitions = new LinkedHashMap<String, BeanDefinition>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        postProcessBeanFactory(beanFactory, (BeanDefinitionRegistry)beanFactory);
    }

    @Override
    public void setEnvironment(Environment environment) {

    }

    private void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory, BeanDefinitionRegistry registry) {
        MultipleBeanDefinitionBuilder beanDefinitionBuilder = new MultipleBeanDefinitionBuilder(
            this.applicationContext);

        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(beanName);

            String beanClassName = definition.getBeanClassName();
            // 当用 @Bean 返回的类型是Object时，beanClassName是 null
            if (beanClassName != null) {
                Class<?> clazz = ClassUtils.resolveClassName(definition.getBeanClassName(), this.classLoader);
                ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
                    @Override
                    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                        MultipleDataSourceProcess.this.parseElement(definition, clazz, method, beanDefinitionBuilder);
                    }
                });
            }
        }

        registry.registerBeanDefinition(lowerFirst(), beanDefinitionBuilder.build());

    }

    private void parseElement(BeanDefinition definition, Class clazz, Method method,
                              MultipleBeanDefinitionBuilder beanDefinitionBuilder) {
        HyjalDataSource annotation = AnnotationUtils.getAnnotation(method, HyjalDataSource.class);
        if (annotation == null) {
            return;
        }

        logger.info("class name is : {}", clazz.getName());
        logger.info("method name is : {}", method.getName());

        if (annotation.isDefault()) {
            beanDefinitionBuilder.defaultTargetDataSource(method.getName());
        }

        beanDefinitions.put(method.getName(), definition);

        beanDefinitionBuilder.targetDataSources(method.getName(), definition);
    }

    private String lowerFirst() {
        char[] chars = MultipleDataSource.class.getSimpleName().toCharArray();
        chars[0] += 32;
        return new String(chars);
    }

}
