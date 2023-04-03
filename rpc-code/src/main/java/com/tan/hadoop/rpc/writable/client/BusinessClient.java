package com.tan.hadoop.rpc.writable.client;

import com.tan.hadoop.rpc.writable.BusinessProtocol;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.net.InetSocketAddress;

public class BusinessClient {

    public static void main(String[] args) {

        try {
            /**
             * 获取 rpc client proxy
             */
            BusinessProtocol proxy = RPC.getProxy(
                    BusinessProtocol.class,
                    BusinessProtocol.versionID,
                    new InetSocketAddress("127.0.0.1", 10001),
                    new Configuration()
            );

            /**
             * 执行业务处理
             */
            proxy.mkdirs("/usr/root");
            String response = proxy.getName("hello-server");
            System.out.println("client revived request response with " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
