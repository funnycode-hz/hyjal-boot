package com.funnycode.hyjal.file.service;

import com.funnycode.hyjal.file.model.BucketAdapter;
import com.funnycode.hyjal.file.model.HttpMethodAdapter;
import com.funnycode.hyjal.file.model.ObjectAdapter;
import com.funnycode.hyjal.file.model.ObjectListingAdapter;
import com.funnycode.hyjal.file.model.ObjectMetadataAdapter;
import com.funnycode.hyjal.file.model.PutObjectResultAdapter;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 文件存储服务接口
 *
 * @author tc
 * @date 2019-03-19
 */
public interface IFileStoreService {

    boolean doesObjectExist(String bucketName, String key);

    boolean deleteObject(String bucketName, String key);

    boolean moveObject(String sourceBucket, String sourceKey, String targetBucket,
                       String targetKey);

    boolean moveObjectAndDeleteOld(String sourceBucket, String sourceKey,
                                   String targetBucket,
                                   String targetKey);

    String getPresignedUrl(String bucketName, String key);

    String getPresignedUrl(String bucketName, String key, String contentType);

    String getPresignedUrl(String bucketName, String key, String contentType,
                           HttpMethodAdapter httpMethod);

    String getPresignedUrl(String bucketName, String key, Date date, String contentType,
                           HttpMethodAdapter httpMethod);

    ObjectAdapter getObject(String bucketName, String key);

    PutObjectResultAdapter putObject(String bucketName, String key, File file);

    PutObjectResultAdapter putObject(String bucketName, String key, InputStream inputStream);

    PutObjectResultAdapter putObject(String bucketName, String key, InputStream inputStream,
                                     ObjectMetadataAdapter metadata);

    List<BucketAdapter> listBuckets();

    ObjectListingAdapter listObjects(String bucketName);

    ObjectListingAdapter listObjects(String bucketName, String prefix);

}
