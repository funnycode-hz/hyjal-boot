package com.funnycode.hyjal.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 海加尔山数据源注解
 * <br>
 * 用来配合Bean说明多数据源的
 *
 * @author tc
 * @date 2019-03-21
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HyjalDataSource {

    /**
     * 名称 数据源的别名
     *
     * @return
     */
    String name() default "";

    /**
     * 是否是默认的数据源
     *
     * @return
     */
    boolean isDefault() default false;

}
