package com.funnycode.hyjal.file.annotation;

import com.funnycode.hyjal.file.FileStoreEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 存储类型注解
 *
 * @author tc
 * @date 2019-03-19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface StoreType {

    /**
     * 存储类型
     *
     * @return
     */
    FileStoreEnum type();

}
