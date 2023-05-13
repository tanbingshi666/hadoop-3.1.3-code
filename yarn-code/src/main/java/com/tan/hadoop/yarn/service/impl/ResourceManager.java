package com.tan.hadoop.yarn.service.impl;

import com.tan.hadoop.yarn.service.CompositeService;
import org.apache.hadoop.conf.Configuration;

/**
 * 组合服务
 */
public class ResourceManager extends CompositeService {

    public ResourceManager(String name) {
        super(name);
    }

    public static void main(String[] args) {

        ResourceManager resourceManager = new ResourceManager("ResourceManager");
        // ResourceManager 服务添加子服务 (可以是组合服务)
        resourceManager.addService(new AService("AService"));
        resourceManager.addService(new BService("BService"));
        resourceManager.addService(new CService("CService"));

        // 初始化服务 (先初始化子服务再启动该服务)
        resourceManager.init(new Configuration());

        // 启动服务 (先启动子服务再启动该服务)
        resourceManager.start();

        // 停止服务 (先停止子服务再启动该服务)
        resourceManager.stop();

    }

}
