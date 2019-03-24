package com.funnycode.hyjal.file;

/**
 * 文件类型枚举
 *
 * @author tc
 * @date 2019-03-19
 */
public enum FileStoreEnum {

    OSS(1, "OSS"),
    Ceph(2, "CEPH");

    FileStoreEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
