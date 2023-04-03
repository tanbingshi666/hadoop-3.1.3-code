package com.tan.hadoop.rpc.proto.server;

import com.google.protobuf.BlockingService;
import com.tan.hadoop.rpc.proto.ResourceTracker;
import com.tan.hadoop.rpc.proto.ResourceTrackerPB;
import com.tan.hadoop.rpc.proto.ResourceTrackerProtocolImpl;
import com.tan.hadoop.rpc.proto.ResourceTrackerServerSidePB;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtobufRpcEngine;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

public class ProtobufRpcServer {

    public static void main(String[] args) throws IOException {

        Configuration conf = new Configuration();
        String hostname = "localhost";
        int port = 9998;
        RPC.setProtocolEngine(conf, ResourceTrackerPB.class, ProtobufRpcEngine.class);

        // 构建 Rpc Server
        RPC.Server server = new RPC.Builder(conf)
                .setProtocol(ResourceTrackerPB.class)
                .setInstance((BlockingService) ResourceTracker.ResourceTrackerService
                        .newReflectiveBlockingService(new ResourceTrackerServerSidePB(new ResourceTrackerProtocolImpl())))
                .setBindAddress(hostname)
                .setPort(port)
                .setNumHandlers(1)
                .setVerbose(true)
                .build();

        // Rpc Server 启动
        server.start();
    }

}
