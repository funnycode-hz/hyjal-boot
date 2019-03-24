package com.funnycode.hyjal.file.service;

import com.funnycode.hyjal.file.FileStoreEnum;
import com.funnycode.hyjal.file.FileStoreProperties;
import com.funnycode.hyjal.file.core.FileStoreServiceFactory;
import com.funnycode.hyjal.file.model.BucketAdapter;
import com.funnycode.hyjal.file.model.HttpMethodAdapter;
import com.funnycode.hyjal.file.model.ObjectAdapter;
import com.funnycode.hyjal.file.model.ObjectListingAdapter;
import com.funnycode.hyjal.file.model.ObjectMetadataAdapter;
import com.funnycode.hyjal.file.model.PutObjectResultAdapter;
import com.funnycode.hyjal.file.utils.Reflection2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * 文件存储基本扩展类
 *
 * @author tc
 * @date 2019-03-19
 */
public abstract class CommonFileStoreService implements IFileStoreService {

    private static final Logger logger = LoggerFactory.getLogger(CommonFileStoreService.class);

    public CommonFileStoreService() {
        this.properties = new FileStoreProperties();
        this.properties.setType(FileStoreEnum.OSS.getName());
    }

    public CommonFileStoreService(FileStoreProperties properties) {
        this.properties = properties;
        init(properties);
    }

    protected abstract void init(FileStoreProperties properties);

    protected FileStoreProperties properties;

    public abstract boolean doDoesObjectExist(String bucketName, String key);

    public abstract boolean doDeleteObject(String bucketName, String key);

    public abstract boolean doMoveObject(String sourceBucket, String sourceKey, String targetBucket,
                                         String targetKey);

    public abstract String doGeneratePresignedUrl(String bucketName, String key, Date date, String contentType,
                                                  HttpMethodAdapter httpMethod);

    public abstract ObjectAdapter doGetObject(String bucketName, String key);

    public abstract PutObjectResultAdapter doPutObject(String bucketName, String key, File file);

    public abstract PutObjectResultAdapter doPutObject(String bucketName, String key, InputStream inputStream,
                                                       ObjectMetadataAdapter metadata);

    private List<BucketAdapter> doListBuckets() {
        Object client = getClient();
        Method listBuckets = ReflectionUtils.findMethod(getClientClass(), "listBuckets");

        return BucketAdapter.adapter(ReflectionUtils.invokeMethod(listBuckets, client));
    }

    protected abstract Class getListObjectRequest();

    protected abstract Object getClient();

    protected abstract Class getClientClass();

    private ObjectListingAdapter doListObjects(String bucketName, String prefix) {
        Class listObjectListing = getListObjectRequest();
        try {
            Object o = listObjectListing.newInstance();
            Method setBucketName = Reflection2Utils.findMethod(listObjectListing, "setBucketName", null);
            ReflectionUtils.invokeMethod(setBucketName, o, bucketName);
            if (prefix != null) {
                Method setPrefix = ReflectionUtils.findMethod(listObjectListing, "setPrefix");
                ReflectionUtils.invokeMethod(setPrefix, o, prefix);
            }
            Object client = getClient();
            Method listObjects = ReflectionUtils.findMethod(getClientClass(), "listObjects", listObjectListing);
            return ObjectListingAdapter.adapter(ReflectionUtils.invokeMethod(listObjects, client, o));
        } catch (Exception e) {
            if (e.getMessage().contains("<Code>AccessDenied</Code>")) {
                logger.error("获取对象失败", e);
            }

            return null;
        }
    }

    @Override
    public boolean doesObjectExist(String bucketName, String key) {
        Assert.notNull(bucketName, "bucketName can not be empty");
        Assert.notNull(key, "key can not be empty");

        return doDoesObjectExist(bucketName, key);
    }

    @Override
    public boolean deleteObject(String bucketName, String key) {
        Assert.notNull(bucketName, "bucketName can not be empty");
        Assert.notNull(key, "key can not be empty");

        return doDeleteObject(bucketName, key);
    }

    @Override
    public boolean moveObject(String sourceBucket, String sourceKey, String targetBucket,
                              String targetKey) {
        Assert.notNull(sourceBucket, "sourceBucket can not be empty");
        Assert.notNull(sourceKey, "sourceKey can not be empty");
        Assert.notNull(targetBucket, "targetBucket can not be empty");
        Assert.notNull(targetKey, "targetKey can not be empty");

        return doMoveObject(sourceBucket, sourceKey, targetBucket, targetKey);
    }

    @Override
    public boolean moveObjectAndDeleteOld(String sourceBucket, String sourceKey, String targetBucket,
                                          String targetKey) {
        if (moveObject(sourceBucket, sourceKey, targetBucket, targetKey)) {
            return doDeleteObject(sourceBucket, sourceKey);
        }

        return false;
    }

    @Override
    public String getPresignedUrl(String bucketName, String key) {
        return getPresignedUrl(bucketName, key, "application/octet-stream");
    }

    @Override
    public String getPresignedUrl(String bucketName, String key, String contentType) {
        return getPresignedUrl(bucketName, key, contentType, HttpMethodAdapter.PUT);
    }

    @Override
    public String getPresignedUrl(String bucketName, String key, String contentType, HttpMethodAdapter httpMethod) {
        return getPresignedUrl(bucketName, key, contentType, httpMethod);
    }

    @Override
    public String getPresignedUrl(String bucketName, String key, Date date, String contentType,
                                  HttpMethodAdapter httpMethod) {
        Assert.notNull(bucketName, "bucketName can not be empty");
        Assert.notNull(key, "key can not be empty");

        return doGeneratePresignedUrl(bucketName, key, date, contentType, httpMethod);
    }

    @Override
    public ObjectAdapter getObject(String bucketName, String key) {
        Assert.notNull(bucketName, "bucketName can not be empty");
        Assert.notNull(key, "key can not be empty");

        return doGetObject(bucketName, key);
    }

    @Override
    public PutObjectResultAdapter putObject(String bucketName, String key, File file) {
        Assert.notNull(bucketName, "bucketName can not be empty");
        Assert.notNull(key, "key can not be empty");

        return doPutObject(bucketName, key, file);
    }

    @Override
    public PutObjectResultAdapter putObject(String bucketName, String key, InputStream inputStream) {
        Assert.notNull(bucketName, "bucketName can not be empty");
        Assert.notNull(key, "key can not be empty");

        return doPutObject(bucketName, key, inputStream, new ObjectMetadataAdapter());
    }

    @Override
    public PutObjectResultAdapter putObject(String bucketName, String key, InputStream inputStream,
                                            ObjectMetadataAdapter metadata) {
        Assert.notNull(bucketName, "bucketName can not be empty");
        Assert.notNull(key, "key can not be empty");

        return doPutObject(bucketName, key, inputStream, metadata);
    }

    @Override
    public List<BucketAdapter> listBuckets() {

        return doListBuckets();
    }

    @Override
    public ObjectListingAdapter listObjects(String bucketName) {
        Assert.notNull(bucketName, "bucketName can not be empty");

        return listObjects(bucketName, null);
    }

    @Override
    public ObjectListingAdapter listObjects(String bucketName, String prefix) {

        return doListObjects(bucketName, prefix);
    }

}
