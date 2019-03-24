package com.funnycode.hyjal.db.annotation;

import com.funnycode.hyjal.db.core.MultipleDataSourceRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动注解
 *
 * @author tc
 * @date 2019-03-24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(MultipleDataSourceRegister.class)
public @interface EnableHyjalDataSource {

}
