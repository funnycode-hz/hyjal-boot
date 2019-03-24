package com.funnycode.hyjal.file.model;

import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author tc
 * @date 2019-03-22
 */
public final class ObjectListingAdapter {

    /**
     * A list of summary information describing the objects stored in the bucket
     */
    private List<ObjectSummaryAdapter> objectSummaries = new ArrayList<>();

    private List<String> commonPrefixes = new ArrayList<String>();

    private String bucketName;

    private String nextMarker;

    private boolean isTruncated;

    private String prefix;

    private String marker;

    private int maxKeys;

    private String delimiter;

    private String encodingType;

    public List<ObjectSummaryAdapter> getObjectSummaries() {
        return objectSummaries;
    }

    public void setObjectSummaries(List<ObjectSummaryAdapter> objectSummaries) {
        this.objectSummaries = objectSummaries;
    }

    public List<String> getCommonPrefixes() {
        return commonPrefixes;
    }

    public void setCommonPrefixes(List<String> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public void setNextMarker(String nextMarker) {
        this.nextMarker = nextMarker;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean truncated) {
        isTruncated = truncated;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public static ObjectListingAdapter adapter(Object object) {
        ObjectListingAdapter objectListingAdapter = new ObjectListingAdapter();
        List<ObjectSummaryAdapter> objectSummaryAdapters = new ArrayList<>();
        if (object instanceof ObjectListing) {
            ObjectListing o = (ObjectListing)object;
            objectListingAdapter.setBucketName(o.getBucketName());
            objectListingAdapter.setCommonPrefixes(o.getCommonPrefixes());
            objectListingAdapter.setPrefix(o.getPrefix());
            objectListingAdapter.setEncodingType(o.getEncodingType());
            ObjectSummaryAdapter objectSummaryAdapter = new ObjectSummaryAdapter();
            BeanUtils.copyProperties(o, objectSummaryAdapter);
            objectListingAdapter.setObjectSummaries(objectSummaryAdapters);
        } else if (object instanceof com.amazonaws.services.s3.model.ObjectListing) {
            com.amazonaws.services.s3.model.ObjectListing o = (com.amazonaws.services.s3.model.ObjectListing)object;
            objectListingAdapter.setBucketName(o.getBucketName());
            objectListingAdapter.setCommonPrefixes(o.getCommonPrefixes());
            objectListingAdapter.setPrefix(o.getPrefix());
            objectListingAdapter.setEncodingType(o.getEncodingType());
            ObjectSummaryAdapter objectSummaryAdapter = new ObjectSummaryAdapter();
            BeanUtils.copyProperties(o, objectSummaryAdapter);
            objectListingAdapter.setObjectSummaries(objectSummaryAdapters);
        }

        return objectListingAdapter;
    }

}
