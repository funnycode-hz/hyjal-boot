package com.funnycode.hyjal.file.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.funnycode.hyjal.file.FileStoreEnum;
import com.funnycode.hyjal.file.FileStoreProperties;
import com.funnycode.hyjal.file.annotation.StoreType;
import com.funnycode.hyjal.file.model.HttpMethodAdapter;
import com.funnycode.hyjal.file.model.ObjectAdapter;
import com.funnycode.hyjal.file.model.ObjectListingAdapter;
import com.funnycode.hyjal.file.model.ObjectMetadataAdapter;
import com.funnycode.hyjal.file.model.PutObjectResultAdapter;
import com.funnycode.hyjal.file.utils.FileStoreClientCache;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * @author tc
 * @date 2019-03-19
 */
@StoreType(type = FileStoreEnum.Ceph)
public class S3Service extends CommonFileStoreService implements IFileStoreService {

    private AmazonS3 client;

    public S3Service() {
    }

    public S3Service(FileStoreProperties properties) {
        super(properties);
    }

    @Override
    protected void init(FileStoreProperties properties) {
        this.client = FileStoreClientCache.getS3Client(properties);
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
        GeneratePresignedUrlRequest request = (date == null ? new GeneratePresignedUrlRequest(bucketName, key)
            .withMethod(hm)
            : new GeneratePresignedUrlRequest(bucketName, key).withExpiration(date).withMethod(hm));
        return client.generatePresignedUrl(request)
            .toString();
    }

    @Override
    public ObjectAdapter doGetObject(String bucketName, String key) {
        S3Object object = client.getObject(bucketName, key);
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
            client.putObject(bucketName, key, inputStream, ObjectMetadataAdapter.findS3(metadata)));
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
        return AmazonS3.class;
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
