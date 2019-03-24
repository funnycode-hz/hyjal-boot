package com.funnycode.hyjal.db.core;

import com.funnycode.hyjal.db.prop.DataSourceProperties;
import com.funnycode.hyjal.db.prop.MultipleDataSourceProperties;
import com.funnycode.hyjal.db.utils.BinderUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tc
 * @date 2019-03-24
 */
public class MultipleDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(MultipleDataSourceRegister.class);

    /**
     * 配置上下文（也可以理解为配置文件的获取工具）
     */
    private Environment evn;

    /**
     * 别名
     */
    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

    /**
     * 由于部分数据源配置不同，所以在此处添加别名，避免切换数据源出现某些参数无法注入的情况
     */
    static {
        aliases.addAliases("url", new String[] {"jdbc-url"});
        aliases.addAliases("username", new String[] {"user"});
    }

    /**
     * 存储我们注册的数据源
     */
    private Map<String, DataSource> customDataSources = new HashMap<String, DataSource>();

    @Override
    public void setEnvironment(Environment environment) {
        this.evn = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        MultipleDataSourceProperties multipleDataSourceProperties = BinderUtils.bind((ConfigurableEnvironment)evn,
            "spring.hyjal.datasource",
            MultipleDataSourceProperties.class);

        // 获取数据源类型
        String typeStr = multipleDataSourceProperties.getDef().getType();
        // 获取数据源类型
        Class<? extends DataSource> clazz = getDataSourceType(typeStr);

        // 绑定默认数据源参数 也就是主数据源
        MultipleDataSourceContextHolder.dataSourceIds.add(multipleDataSourceProperties.getDef().getKey());
        logger.info("注册默认数据源成功");

        // 获取其他数据源配置
        List<DataSourceProperties> configs = multipleDataSourceProperties.getCluster();
        DataSourceProperties dataSourceProperties;

        DataSource dataSource;

        Map defaultDataSourceMap = new BeanMap(multipleDataSourceProperties.getDef());
        DataSource defaultDatasource = bind(clazz, defaultDataSourceMap);

        // 遍历从数据源
        for (int i = 0; i < configs.size(); i++) {
            dataSourceProperties = configs.get(i);
            clazz = getDataSourceType(dataSourceProperties.getType());

            Map singleDataSourceMap = new BeanMap(dataSourceProperties);
            // 绑定参数
            dataSource = bind(clazz, singleDataSourceMap);
            // 获取数据源的key，以便通过该key可以定位到数据源
            String key = dataSourceProperties.getKey();
            customDataSources.put(key, dataSource);

            // 数据源上下文，用于管理数据源与记录已经注册的数据源key
            MultipleDataSourceContextHolder.dataSourceIds.add(key);
            logger.info("注册数据源{}成功", key);
        }
        // bean定义类
        GenericBeanDefinition define = new GenericBeanDefinition();
        // 设置bean的类型，此处DynamicRoutingDataSource是继承AbstractRoutingDataSource的实现类
        define.setBeanClass(MultipleDataSource.class);
        // 需要注入的参数
        MutablePropertyValues mpv = define.getPropertyValues();
        // 添加默认数据源，避免key不存在的情况没有数据源可用
        mpv.add("defaultTargetDataSource", defaultDatasource);
        // 添加其他数据源
        mpv.add("targetDataSources", customDataSources);
        // 将该bean注册为datasource，不使用springboot自动生成的datasource
        registry.registerBeanDefinition("datasource", define);
        logger.info("注册数据源成功，一共注册{}个数据源", customDataSources.keySet().size() + 1);
    }

    private void bind(DataSource result, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[] {source.withAliases(aliases)});
        // 将参数绑定到对象
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[] {source.withAliases(aliases)});
        // 通过类型绑定参数并获得实例对象
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get();
    }

    //private <T extends DataSource> T bind(Class<T> clazz, String sourcePath) {
    //    Map properties = binder.bind(sourcePath, Map.class).get();
    //    return bind(clazz, properties);
    //}

    /**
     * 通过字符串获取数据源class对象
     *
     * @param typeStr
     * @return
     */
    private Class<? extends DataSource> getDataSourceType(String typeStr) {
        Class<? extends DataSource> type;
        try {
            if (StringUtils.hasLength(typeStr)) {
                // 字符串不为空则通过反射获取class对象
                type = (Class<? extends DataSource>)Class.forName(typeStr);
            } else {
                // 默认为hikariCP数据源，与springboot默认数据源保持一致
                type = HikariDataSource.class;
            }
            return type;
        } catch (Exception e) {
            throw new IllegalArgumentException("can not resolve class with type: "
                + typeStr); //无法通过反射获取class对象的情况则抛出异常，该情况一般是写错了，所以此次抛出一个runtimeexception
        }
    }

}
