package com.funnycode.hyjal.db.aop;

import com.funnycode.hyjal.db.annotation.HyjalDataSource;
import com.funnycode.hyjal.db.core.MultipleDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author tc
 * @date 2019-03-24
 */
@Aspect
@Component
public class MultipleDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(MultipleDataSourceAspect.class);

    @Before("@annotation(hyjalDataSource)")
    public void changeDataSource(JoinPoint point, HyjalDataSource hyjalDataSource) throws Throwable {
        String dsId = hyjalDataSource.name();
        if (MultipleDataSourceContextHolder.dataSourceIds.contains(dsId)) {
            MultipleDataSourceContextHolder.setDataSourceRouterKey(dsId);
            logger.debug("Use DataSource :{} >", dsId, point.getSignature());
        } else {
            logger.info("数据源[{}]不存在，使用默认数据源 >{}", dsId, point.getSignature());
        }
    }

    @After("@annotation(hyjalDataSource)")
    public void restoreDataSource(JoinPoint point, HyjalDataSource hyjalDataSource) {
        logger.debug("Revert DataSource : " + hyjalDataSource.name() + " > " + point.getSignature());
        MultipleDataSourceContextHolder.removeDataSourceRouterKey();
    }

}
