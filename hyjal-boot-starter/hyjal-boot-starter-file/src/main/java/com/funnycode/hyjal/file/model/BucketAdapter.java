package com.funnycode.hyjal.file.model;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Owner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tc
 * @date 2019-03-22
 */
public final class BucketAdapter {

    private final static String amazonName = Bucket.class.getName();

    private final static String ossName = com.aliyun.oss.model.Bucket.class.getName();

    /**
     * The name of this S3 bucket
     */
    private String name = null;

    /**
     * The details on the owner of this bucket
     */
    private Owner owner = null;

    /**
     * The date this bucket was created
     */
    private Date creationDate = null;

    /**
     * Bucket location
     */
    private String location;

    // External endpoint.It could be accessed from anywhere.
    private String extranetEndpoint;

    // Internal endpoint. It could be accessed within AliCloud under the same
    // location.
    private String intranetEndpoint;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExtranetEndpoint() {
        return extranetEndpoint;
    }

    public void setExtranetEndpoint(String extranetEndpoint) {
        this.extranetEndpoint = extranetEndpoint;
    }

    public String getIntranetEndpoint() {
        return intranetEndpoint;
    }

    public void setIntranetEndpoint(String intranetEndpoint) {
        this.intranetEndpoint = intranetEndpoint;
    }

    public static List<BucketAdapter> adapter(Object object) {
        List result = null;
        List<BucketAdapter> bucketAdapters = new ArrayList<>();
        if (object instanceof List) {
            result = (List)object;
            String name = result.get(0).getClass().getName();
            if (name.equals(amazonName)) {
                List<Bucket> r = result;
                r.stream().forEach(x -> {
                    BucketAdapter bucketAdapter = new BucketAdapter();
                    bucketAdapter.setCreationDate(x.getCreationDate());
                    bucketAdapter.setOwner(x.getOwner());
                    bucketAdapters.add(bucketAdapter);
                });
            } else if (name.equals(ossName)) {
                List<com.aliyun.oss.model.Bucket> r = result;
                r.stream().forEach(x -> {
                    BucketAdapter bucketAdapter = new BucketAdapter();
                    bucketAdapter.setName(x.getName());
                    bucketAdapter.setCreationDate(x.getCreationDate());
                    bucketAdapter.setLocation(x.getLocation());
                    bucketAdapter.setExtranetEndpoint(x.getExtranetEndpoint());
                    bucketAdapter.setIntranetEndpoint(x.getIntranetEndpoint());
                    bucketAdapters.add(bucketAdapter);
                });
            }
        } else {
            // 不正常
        }

        return bucketAdapters;
    }

}
