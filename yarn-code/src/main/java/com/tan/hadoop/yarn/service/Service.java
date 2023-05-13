package com.tan.hadoop.yarn.service;

import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

/**
 * hadoop-yarn 服务接口
 */
public interface Service {

    /**
     * 服务状态枚举
     */
    public enum STATE {

        /**
         * 调用服务构造函数
         */
        NOTINITED(0, "NOTINITED"),

        /**
         * 调用服务 serviceInit()
         */
        INITED(1, "INITED"),

        /**
         * 调用服务的 serviceStart()
         */
        STARTED(2, "STARTED"),

        /**
         * 调用服务的 serviceStop()
         */
        STOPPED(3, "STOPPED");

        private final int value;
        private final String statename;

        private STATE(int value, String name) {
            this.value = value;
            this.statename = name;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return statename;
        }
    }

    // 服务生命周期调用
    void init(Configuration config);

    void start();

    void stop();

    void close() throws IOException;

    STATE getServiceState();

}
