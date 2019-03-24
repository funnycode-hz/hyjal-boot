package com.funnycode.hyjal.file.core;

import com.funnycode.hyjal.file.FileStoreProperties;
import com.funnycode.hyjal.file.service.IFileStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;

/**
 * 文件服务存储工厂
 *
 * @author tc
 * @date 2019-03-19
 */
public class FileStoreServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileStoreServiceFactory.class);

    private FileStoreServiceFactory() {
    }

    public static IFileStoreService getInstance(FileStoreProperties properties) {
        Assert.notNull(properties.getType(), "Invalid file-store type");
        Assert.notNull(properties.getAccessKey(), "Invalid file-store accessKey");
        Assert.notNull(properties.getSecretKey(), "Invalid file-store secretKey");
        Assert.notNull(properties.getEndPoint(), "Invalid file-store endPointKey");

        return doGetInstance(properties);
    }

    private static IFileStoreService doGetInstance(FileStoreProperties properties) {
        try {
            Class<?> clazz = FileStoreServiceImpProcess.getFileStoreClassByType(properties.getType());
            Constructor constructor = clazz.getConstructor(properties.getClass());
            return (IFileStoreService)constructor.newInstance(properties);
        } catch (Exception e) {
            logger.error("Error occurred while init producer instance, FileStoreType type is {}",
                properties.getType(), e);
            return null;
        }
    }

}
