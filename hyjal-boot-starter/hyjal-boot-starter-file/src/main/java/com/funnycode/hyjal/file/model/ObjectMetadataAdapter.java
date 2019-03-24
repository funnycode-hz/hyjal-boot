package com.funnycode.hyjal.file.model;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <br>
 * {@link com.amazonaws.services.s3.model.ObjectMetadata}
 * <br>
 * {@link com.aliyun.oss.model.ObjectMetadata}
 *
 * @author tc
 * @date 2019-03-19
 */
public class ObjectMetadataAdapter {

    // 用户自定义的元数据，表示以x-oss-meta-为前缀的请求头。
    private Map<String, String> userMetadata = new HashMap<String, String>();

    // 非用户自定义的元数据。
    private Map<String, Object> metadata = new HashMap<String, Object>();

    public Map<String, String> getUserMetadata() {
        return userMetadata;
    }

    public void setUserMetadata(Map<String, String> userMetadata) {
        this.userMetadata = userMetadata;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public static ObjectMetadataAdapter adapter(Object object) {
        ObjectMetadataAdapter objectMetadataAdapter = new ObjectMetadataAdapter();
        if (object instanceof com.aliyun.oss.model.ObjectMetadata) {
            com.aliyun.oss.model.ObjectMetadata objectMetadata
                = (com.aliyun.oss.model.ObjectMetadata)object;
            objectMetadataAdapter.setUserMetadata(objectMetadata.getUserMetadata());
            objectMetadataAdapter.setMetadata(objectMetadata.getRawMetadata());
        } else if (object instanceof com.amazonaws.services.s3.model.ObjectMetadata) {
            com.amazonaws.services.s3.model.ObjectMetadata objectMetadata
                = (com.amazonaws.services.s3.model.ObjectMetadata)object;
            objectMetadataAdapter.setUserMetadata(objectMetadata.getUserMetadata());
            objectMetadataAdapter.setMetadata(objectMetadata.getRawMetadata());
        }

        return objectMetadataAdapter;
    }

    public static com.aliyun.oss.model.ObjectMetadata findOss(ObjectMetadataAdapter metadata) {
        com.aliyun.oss.model.ObjectMetadata objectMetadata = new com.aliyun.oss.model.ObjectMetadata();
        objectMetadata.setUserMetadata(metadata.getUserMetadata());
        metadata.getMetadata().forEach((x, y) -> {
            Method setMethod = ReflectionUtils.findMethod(com.aliyun.oss.model.ObjectMetadata.class,
                setMethodNameByField(x), y.getClass());
            ReflectionUtils.invokeMethod(setMethod, objectMetadata, y);
        });

        return objectMetadata;
    }

    public static ObjectMetadata findS3(ObjectMetadataAdapter metadata) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setUserMetadata(metadata.getUserMetadata());
        metadata.getMetadata().forEach((x, y) -> {
            Method setMethod = ReflectionUtils.findMethod(ObjectMetadata.class, setMethodNameByField(x), y.getClass());
            ReflectionUtils.invokeMethod(setMethod, objectMetadata, y);
        });

        return objectMetadata;
    }

    private static String getMethodNameByField(String fieldName) {
        System.out.println("字段名称" + fieldName);
        fieldName = fieldName.replace("-", "");
        char[] chars = fieldName.toCharArray();
        chars[0] += 32;
        return "get" + String.valueOf(chars);
    }

    private static String setMethodNameByField(String fieldName) {
        System.out.println("字段名称" + fieldName);
        fieldName = fieldName.replace("-", "");
        char[] chars = fieldName.toCharArray();
        chars[0] += 32;
        return "set" + String.valueOf(chars);
    }

}
