package com.funnycode.hyjal.file.service;

import com.funnycode.hyjal.file.FileStoreEnum;
import com.funnycode.hyjal.file.FileStoreProperties;
import com.funnycode.hyjal.file.config.Config;
import com.funnycode.hyjal.file.core.FileStoreServiceFactory;
import com.funnycode.hyjal.file.model.BucketAdapter;
import com.funnycode.hyjal.file.model.ObjectListingAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author tc
 * @date 2019-03-22
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = Config.class)
public class OssServiceTest {

    private IFileStoreService fileStoreService;

    @Before
    public void init() {
        FileStoreProperties fileStoreProperties = new FileStoreProperties();
        fileStoreProperties.setType(FileStoreEnum.OSS.getName());
        fileStoreProperties.setAccessKey("LTAIfbnbWID59wvR");
        fileStoreProperties.setSecretKey("85cNWjjb9vXKIKUZxJHqN89fwGAZbY");
        fileStoreProperties.setEndPoint("oss-cn-beijing.aliyuncs.com");
        fileStoreService = FileStoreServiceFactory.getInstance(fileStoreProperties);
    }

    @Test
    public void test14() {
        List<BucketAdapter> bucketAdapters = fileStoreService.listBuckets();
        for (BucketAdapter bucketAdapter : bucketAdapters) {
            ObjectListingAdapter objectListingAdapter = fileStoreService.listObjects(bucketAdapter.getName());
            System.out.println("bucketName " + bucketAdapter.getName());
            if (objectListingAdapter != null) {
                System.out.println("ObjectSize " + objectListingAdapter.getObjectSummaries().size());
            }
        }
        System.out.println("bucketSize " + bucketAdapters.size());
    }

}