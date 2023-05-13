package com.tan.hadoop.yarn.service;

import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

/**
 * 抽象的服务对象 (主要提供服务状态的改变功能)
 */
public abstract class AbstractService implements Service {

    private final String name;
    private volatile Configuration config;
    private final ServiceStateModel stateModel;

    public AbstractService(String name) {
        this.name = name;
        stateModel = new ServiceStateModel(name);
    }

    protected void setConfig(Configuration conf) {
        this.config = conf;
    }

    @Override
    public void init(Configuration conf) {
        setConfig(conf);
        serviceInit(config);
    }

    protected void serviceInit(Configuration config) {
        setConfig(config);
        enterState(STATE.INITED);
    }

    @Override
    public void start() {
        try {
            serviceStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void serviceStart() throws Exception {
        enterState(STATE.STARTED);
    }

    @Override
    public void stop() {
        try {
            serviceStop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void serviceStop() throws Exception {
        enterState(STATE.STOPPED);
    }

    @Override
    public final void close() throws IOException {
        stop();
    }

    public String getName() {
        return name;
    }

    private STATE enterState(STATE newState) {
        return stateModel.enterState(newState);
    }

    @Override
    public final STATE getServiceState() {
        return stateModel.getState();
    }

}
