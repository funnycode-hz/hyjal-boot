package com.funnycode.hyjal.file.model;

import com.amazonaws.services.s3.model.Owner;

import java.util.Date;

/**
 * @author tc
 * @date 2019-03-22
 */
public final class ObjectSummaryAdapter {

    /** The name of the bucket in which this object is stored */
    protected String bucketName;

    /** The key under which this object is stored */
    protected String key;

    /** Hex encoded MD5 hash of this object's contents, as computed by Amazon S3 */
    protected String eTag;

    /** The size of this object, in bytes */
    protected long size;

    /** The date, according to Amazon S3, when this object was last modified */
    protected Date lastModified;

    /** The class of storage used by Amazon S3 to store this object */
    protected String storageClass;

    /**
     * The owner of this object - can be null if the requester doesn't have
     * permission to view object ownership information
     */
    protected Owner owner;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

}
