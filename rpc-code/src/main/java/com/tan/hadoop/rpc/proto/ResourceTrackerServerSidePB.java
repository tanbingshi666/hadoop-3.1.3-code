package com.tan.hadoop.rpc.proto;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class ResourceTrackerServerSidePB implements ResourceTrackerPB {

    final private ResourceTrackerProtocol server;

    public ResourceTrackerServerSidePB(ResourceTrackerProtocol server) {
        this.server = server;
    }

    @Override
    public ResourceTrackerMessage.ResponseProto registerNodeManager(RpcController controller, ResourceTrackerMessage.RequestProto request) throws ServiceException {
        try {
            return server.registerNodeManager(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
