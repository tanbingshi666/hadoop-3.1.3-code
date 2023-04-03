package com.tan.hadoop.rpc.writable;

/**
 * 业务协议接口
 */
public interface BusinessProtocol {

    /**
     * 版本 ID
     */
    long versionID = 123456L;

    void mkdirs(String path);

    String getName(String name);

}
