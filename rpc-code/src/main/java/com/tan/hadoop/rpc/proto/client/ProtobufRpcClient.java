package com.tan.hadoop.rpc.proto.client;

import com.google.protobuf.ServiceException;
import com.tan.hadoop.rpc.proto.ResourceTrackerMessage;
import com.tan.hadoop.rpc.proto.ResourceTrackerPB;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtobufRpcEngine;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ProtobufRpcClient {
    public static void main(String[] args) throws IOException {
        // 设置 RPC 引擎为 ProtobufRpcEngine
        Configuration conf = new Configuration();
        String hostname = "localhost";
        int port = 9998;
        RPC.setProtocolEngine(conf, ResourceTrackerPB.class, ProtobufRpcEngine.class);

        // 获取代理
        ResourceTrackerPB protocolProxy = RPC
                .getProxy(ResourceTrackerPB.class,
                        1,
                        new InetSocketAddress(hostname, port),
                        conf);

        //  构建请求对象
        ResourceTrackerMessage.RequestProto.Builder builder =
                ResourceTrackerMessage.RequestProto
                        .newBuilder();

        ResourceTrackerMessage.RequestProto requestProto = builder
                .setHostname("hadoop")
                .setCpu(64)
                .setMemory(128)
                .build();

        // 发送 RPC 请求，获取响应
        ResourceTrackerMessage.ResponseProto response = null;
        try {
            response = protocolProxy
                    .registerNodeManager(null, requestProto);

            // 处理响应
            String flag = response.getFlag();
            System.out.println("最终注册结果： flag = " + flag);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }
}
