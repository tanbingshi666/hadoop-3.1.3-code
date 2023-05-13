package com.tan.hadoop.yarn.service.impl;

import com.tan.hadoop.yarn.service.AbstractService;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * B 服务
 */
public class BService extends AbstractService {

    private static final Logger LOG = LoggerFactory.getLogger(BService.class);

    public BService(String name) {
        super(name);
    }

    @Override
    protected void serviceInit(Configuration config) {
        // 业务处理逻辑
        // ......
        super.serviceInit(config);
        System.out.println("进入服务状态 " + getServiceState().toString());
    }

    @Override
    protected void serviceStart() throws Exception {
        // 业务处理逻辑
        // ......
        super.serviceStart();
        System.out.println("进入服务状态 " + getServiceState().toString());
    }

    @Override
    protected void serviceStop() throws Exception {
        // 业务处理逻辑
        // ......
        super.serviceStop();
        System.out.println("进入服务状态 " + getServiceState().toString());
    }

}
