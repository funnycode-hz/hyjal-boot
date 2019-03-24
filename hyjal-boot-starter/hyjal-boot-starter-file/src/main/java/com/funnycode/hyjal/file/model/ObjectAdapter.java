package com.funnycode.hyjal.file.model;

import com.aliyun.oss.model.OSSObject;
import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;

/**
 * <br>
 * {@link com.amazonaws.services.s3.model.S3Object}
 * <br>
 * {@link com.aliyun.oss.model.OSSObject}
 *
 * @author tc
 * @date 2019-03-19
 */
public class ObjectAdapter {

    // Object key (name)
    private String key;

    // Object所在的Bucket的名称。
    private String bucketName;

    // Object的元数据。
    private ObjectMetadataAdapter metadata = new ObjectMetadataAdapter();

    // Object所包含的内容。
    private InputStream objectContent;

    public ObjectAdapter() {
    }

    public ObjectAdapter(String key, String bucketName, InputStream objectContent) {
        this.key = key;
        this.bucketName = bucketName;
        this.objectContent = objectContent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public ObjectMetadataAdapter getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMetadataAdapter metadata) {
        this.metadata = metadata;
    }

    public InputStream getObjectContent() {
        return objectContent;
    }

    public void setObjectContent(InputStream objectContent) {
        this.objectContent = objectContent;
    }

    public static ObjectAdapter adapter(Object object) {
        ObjectAdapter objectAdapter = new ObjectAdapter();
        if (object instanceof com.amazonaws.services.s3.model.S3Object) {
            S3Object s3Object = (S3Object)object;
            objectAdapter.setKey(s3Object.getKey());
            objectAdapter.setBucketName(s3Object.getBucketName());
            objectAdapter.setObjectContent(s3Object.getObjectContent());
            objectAdapter.setMetadata(ObjectMetadataAdapter.adapter(s3Object.getObjectMetadata()));
        } else if (object instanceof com.aliyun.oss.model.OSSObject) {
            OSSObject ossObject = (OSSObject)object;
            objectAdapter.setKey(ossObject.getKey());
            objectAdapter.setBucketName(ossObject.getBucketName());
            objectAdapter.setObjectContent(ossObject.getObjectContent());
            objectAdapter.setMetadata(ObjectMetadataAdapter.adapter(ossObject.getObjectMetadata()));
        }

        return objectAdapter;
    }

}
