package com.funnycode.hyjal.file.service;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectListing;
import com.funnycode.hyjal.file.FileStoreEnum;
import com.funnycode.hyjal.file.FileStoreProperties;
import com.funnycode.hyjal.file.annotation.StoreType;
import com.funnycode.hyjal.file.model.HttpMethodAdapter;
import com.funnycode.hyjal.file.FileStoreEnum;
import com.funnycode.hyjal.file.FileStoreProperties;
import com.funnycode.hyjal.file.model.HttpMethodAdapter;
import com.funnycode.hyjal.file.model.ObjectAdapter;
import com.funnycode.hyjal.file.model.ObjectListingAdapter;
import com.funnycode.hyjal.file.model.ObjectMetadataAdapter;
import com.funnycode.hyjal.file.model.PutObjectResultAdapter;
import com.funnycode.hyjal.file.utils.FileStoreClientCache;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author tc
 * @date 2019-03-19
 */
@StoreType(type = FileStoreEnum.OSS)
public class OssService extends CommonFileStoreService implements IFileStoreService {

    private OSSClient client;

    public OssService() {
    }

    public OssService(FileStoreProperties properties) {
        super(properties);
    }

    @Override
    protected void init(FileStoreProperties properties) {
        this.client = FileStoreClientCache.getOssClient(properties);
    }

    @Override
    public boolean doDoesObjectExist(String bucketName, String key) {
        return client.doesObjectExist(bucketName, key);
    }

    @Override
    public boolean doDeleteObject(String bucketName, String key) {
        client.deleteObject(bucketName, key);
        return true;
    }

    @Override
    public boolean doMoveObject(String sourceBucket, String sourceKey, String targetBucket, String targetKey) {
        CopyObjectResult copyObjectResult = client.copyObject(sourceBucket, sourceKey, targetBucket, targetKey);

        return copyObjectResult != null ? true : false;
    }

    @Override
    public String doGeneratePresignedUrl(String bucketName, String key, Date date, String contentType,
                                         HttpMethodAdapter httpMethod) {
        HttpMethod hm = HttpMethod.valueOf(httpMethod.name());
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);
        request.setExpiration(Optional.ofNullable(date).orElseGet(() -> {
            Date d = new Date();
            d.setTime(System.currentTimeMillis() + 3600 * 1000);
            return d;
        }));

        request.setContentType(contentType);
        request.setMethod(hm);
        client.generatePresignedUrl(request);
        return null;
    }

    @Override
    public ObjectAdapter doGetObject(String bucketName, String key) {
        OSSObject object = client.getObject(bucketName, key);
        return ObjectAdapter.adapter(object);
    }

    @Override
    public PutObjectResultAdapter doPutObject(String bucketName, String key, File file) {
        return PutObjectResultAdapter.adapter(client.putObject(bucketName, key, file));
    }

    @Override
    public PutObjectResultAdapter doPutObject(String bucketName, String key, InputStream inputStream,
                                              ObjectMetadataAdapter metadata) {
        return PutObjectResultAdapter.adapter(
            client.putObject(bucketName, key, inputStream, ObjectMetadataAdapter.findOss(metadata)));
    }

    @Override
    protected Class getListObjectRequest() {
        return ListObjectsRequest.class;
    }

    @Override
    protected Object getClient() {
        return client;
    }

    @Override
    protected Class getClientClass() {
        return OSSClient.class;
    }

    //@Override
    public ObjectListingAdapter doListObjects(String bucketName, String prefix) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        if (prefix != null) {
            listObjectsRequest.setPrefix(prefix);
        }

        return ObjectListingAdapter.adapter(client.listObjects(listObjectsRequest));
    }

}
