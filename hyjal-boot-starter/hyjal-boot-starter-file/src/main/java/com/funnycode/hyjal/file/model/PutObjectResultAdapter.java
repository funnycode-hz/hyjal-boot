package com.funnycode.hyjal.file.model;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.util.Date;

/**
 * @author tc
 * @date 2019-03-19
 */
public class PutObjectResultAdapter {

    private String eTag;

    private Date expirationTime;

    private String expirationTimeRuleId;

    private ObjectMetadata metadata;

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getExpirationTimeRuleId() {
        return expirationTimeRuleId;
    }

    public void setExpirationTimeRuleId(String expirationTimeRuleId) {
        this.expirationTimeRuleId = expirationTimeRuleId;
    }

    public ObjectMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMetadata metadata) {
        this.metadata = metadata;
    }

    public static PutObjectResultAdapter adapter(Object objectResult) {
        PutObjectResultAdapter putObjectResultAdapter = new PutObjectResultAdapter();
        if (objectResult instanceof com.aliyun.oss.model.PutObjectResult) {
            String eTag = ((com.aliyun.oss.model.PutObjectResult)objectResult).getETag();
            putObjectResultAdapter.seteTag(eTag);
        } else if (objectResult instanceof com.amazonaws.services.s3.model.PutObjectResult) {
            com.amazonaws.services.s3.model.PutObjectResult putObjectResult
                = (com.amazonaws.services.s3.model.PutObjectResult)objectResult;
            putObjectResultAdapter.seteTag(putObjectResult.getETag());
            putObjectResultAdapter.setExpirationTime(putObjectResult.getExpirationTime());
            putObjectResultAdapter.setMetadata(putObjectResult.getMetadata());
            putObjectResultAdapter.setExpirationTimeRuleId(putObjectResult.getExpirationTimeRuleId());
        }

        return putObjectResultAdapter;
    }

}
