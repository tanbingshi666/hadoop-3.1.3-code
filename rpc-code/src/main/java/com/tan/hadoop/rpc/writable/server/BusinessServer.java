package com.tan.hadoop.rpc.writable.server;

import com.tan.hadoop.rpc.writable.BusinessProtocol;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

public class BusinessServer {

    public static void main(String[] args) {

        try {
            /**
             * 构建 rpc server
             */
            RPC.Server server = new RPC.Builder(new Configuration())
                    .setProtocol(BusinessProtocol.class)
                    .setInstance(new BusinessProtocolImpl())
                    .setBindAddress("127.0.0.1")
                    .setPort(10001)
                    .build();

            /**
             * 启动 rpc server
             */
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
