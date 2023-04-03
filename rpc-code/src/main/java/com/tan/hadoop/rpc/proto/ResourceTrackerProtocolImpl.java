package com.tan.hadoop.rpc.proto;

public class ResourceTrackerProtocolImpl implements ResourceTrackerProtocol {
    @Override
    public ResourceTrackerMessage.ResponseProto registerNodeManager(ResourceTrackerMessage.RequestProto request) throws Exception {
        // 构建一个响应对象，用于返回
        ResourceTrackerMessage.ResponseProto.Builder builder =
                ResourceTrackerMessage.ResponseProto
                        .newBuilder();

        // 输出注册的消息
        String hostname = request.getHostname();
        int cpu = request.getCpu();
        int memory = request.getMemory();
        System.out.println("注册消息： hostname = " + hostname + ", cpu = " + cpu + ", memory = " + memory);

        // 直接暴力返回 True
        builder.setFlag("true");
        return builder.build();
    }
}
