package com.tan.hadoop.rpc.writable.server;

import com.tan.hadoop.rpc.writable.BusinessProtocol;

public class BusinessProtocolImpl implements BusinessProtocol {

    @Override
    public void mkdirs(String path) {
        System.out.println("server revived mkdirs request with path " + path);
    }

    @Override
    public String getName(String name) {
        System.out.println("server revived getName request");
        return "server name";
    }
}
