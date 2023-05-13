package com.tan.hadoop.yarn.service;

/**
 * 服务状态模型
 */
public class ServiceStateModel {

    private volatile Service.STATE state;
    private final String name;

    public ServiceStateModel(String name) {
        this(name, Service.STATE.NOTINITED);
    }

    public ServiceStateModel(String name, Service.STATE state) {
        this.state = state;
        this.name = name;
    }

    public Service.STATE getState() {
        return state;
    }

    public synchronized Service.STATE enterState(Service.STATE newState) {
        Service.STATE oldState = state;
        state = newState;
        return oldState;
    }

}
