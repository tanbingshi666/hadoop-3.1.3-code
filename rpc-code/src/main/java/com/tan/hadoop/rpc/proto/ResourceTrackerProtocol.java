package com.tan.hadoop.rpc.proto;

public interface ResourceTrackerProtocol {

    ResourceTrackerMessage.ResponseProto registerNodeManager(ResourceTrackerMessage.RequestProto request) throws Exception;

}
