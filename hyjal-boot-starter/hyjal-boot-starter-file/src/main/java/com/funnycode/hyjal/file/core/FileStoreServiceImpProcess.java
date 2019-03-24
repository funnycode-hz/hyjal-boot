package com.funnycode.hyjal.file.core;

import com.funnycode.hyjal.file.annotation.StoreType;
import com.funnycode.hyjal.file.service.IFileStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tc
 * @date 2019-03-19
 */
public class FileStoreServiceImpProcess {

    private static final Logger logger = LoggerFactory.getLogger(FileStoreServiceImpProcess.class);

    private static Map<String, Class<?>> fileStoreMap = new ConcurrentHashMap<>();

    static {
        ServiceLoader<IFileStoreService> matcher = ServiceLoader.load(IFileStoreService.class);
        Iterator<IFileStoreService> matcherIter = matcher.iterator();
        while (matcherIter.hasNext()) {
            IFileStoreService wordMatcher = matcherIter.next();
            logger.info("加载到的类 {}", wordMatcher.getClass().getName());

            StoreType annotation = wordMatcher.getClass().getAnnotation(StoreType.class);
            if (annotation != null) {
                fileStoreMap.putIfAbsent(annotation.type().getName(), wordMatcher.getClass());
            }
        }

    }

    public static Class<?> getFileStoreClassByType(String type) {
        return fileStoreMap.get(type);
    }

}
