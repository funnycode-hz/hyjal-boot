package com.funnycode.hyjal.file.utils;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.funnycode.hyjal.file.FileStoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局文件 连接对象池
 *
 * @author tc
 * @date 2019-03-19
 */
public final class FileStoreClientCache {

    private static transient Logger logger = LoggerFactory.getLogger(FileStoreClientCache.class);

    private static final String DEFAULT_SEPARATE_SIGN = "||";

    private static final String DEFAULT_SEPARATE_PREFIX = "__FS_CLIENT__";

    private static final String DEFAULT_SEPARATE_END = "__FS_END";

    private static Map<String, OSSClient> ossClientMap = new ConcurrentHashMap<>(8);

    private static Map<String, AmazonS3> amazonS3Map = new ConcurrentHashMap<>(8);

    public static OSSClient getOssClient(FileStoreProperties store) {
        String key = key(store.getAccessKey(), store.getSecretKey(), store.getEndPoint());
        OSSClient client = ossClientMap.get(key);
        if (client == null) {
            synchronized (FileStoreClientCache.class) {
                if (client == null) {
                    client = createNewOssClient(store.getAccessKey(), store.getSecretKey(), store.getEndPoint());
                    ossClientMap.putIfAbsent(key, client);
                }
            }
        }

        return client;
    }

    private static OSSClient createNewOssClient(String ossKey, String ossSecret, String endpoint) {
        if (StringUtils.isBlank(endpoint)) {
            return new OSSClient(ossKey, ossSecret);
        } else {
            return new OSSClient(endpoint, ossKey, ossSecret);
        }
    }

    public static AmazonS3 getS3Client(FileStoreProperties store) {
        String key = key(store.getAccessKey(), store.getSecretKey(), store.getEndPoint());
        AmazonS3 amazonS3 = amazonS3Map.get(key);
        if (amazonS3 == null) {
            synchronized (FileStoreClientCache.class) {
                if (amazonS3 == null) {
                    amazonS3 = createNewS3Client(store.getAccessKey(), store.getSecretKey(), store.getEndPoint());
                    amazonS3Map.putIfAbsent(key, amazonS3);
                }
            }
        }

        return amazonS3;
    }

    private static AmazonS3 createNewS3Client(String accessKey, String secretKey, String endpointUrl) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
            endpointUrl, Regions.CN_NORTH_1.getName());
        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(endpointConfiguration)
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .build();
    }

    public static String key(String key, String secret, String endPoint) {
        return concat(DEFAULT_SEPARATE_SIGN, new String[] {key, secret, endPoint}, null);
    }

    public static String key(String key, String secret, String endPoint, Map<String, Object> params) {
        return concat(DEFAULT_SEPARATE_SIGN, new String[] {key, secret, endPoint}, params);
    }

    private static String concat(String separate, String[] baseParam, Map<String, Object> params) {
        int length = baseParam.length;
        String key = DEFAULT_SEPARATE_PREFIX;
        for (int i = 0; i < length; i++) {
            key += baseParam[i] + separate;
        }

        if (!CollectionUtils.isEmpty(params)) {
            key += JSON.toJSONString(params) + separate;
        }

        key += DEFAULT_SEPARATE_END;

        return key;
    }

    public static void shotDown() {
        synchronized (FileStoreClientCache.class) {
            logger.info("file-store start clear, please wait..., time is : {}", System.currentTimeMillis());
            ossClientMap.forEach((x, y) -> {
                y.shutdown();
            });
            amazonS3Map.forEach((x, y) -> {
                y.shutdown();
            });

            ossClientMap.clear();
            amazonS3Map.clear();
            logger.info("file-store clear over, time is : {}", System.currentTimeMillis());
        }
    }

}
