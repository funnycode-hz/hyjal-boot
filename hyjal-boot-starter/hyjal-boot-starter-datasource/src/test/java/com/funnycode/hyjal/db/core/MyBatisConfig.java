package com.funnycode.hyjal.db.core;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.funnycode.hyjal.db.annotation.HyjalDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author tc
 * @date 2019-03-24
 */
@Configuration
@MapperScan(basePackages = "com.funnycode.hyjal.db.core")
public class MyBatisConfig {

}
