package com.tan.hadoop.yarn.service;

import org.apache.hadoop.conf.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合服务 (本身是一个服务，该服务维护很多其他服务)
 */
public class CompositeService extends AbstractService {

    private final List<Service> serviceList = new ArrayList<Service>();

    public CompositeService(String name) {
        super(name);
    }

    public List<Service> getServices() {
        synchronized (serviceList) {
            return new ArrayList<Service>(serviceList);
        }
    }

    protected void addService(Service service) {
        synchronized (serviceList) {
            serviceList.add(service);
        }
    }

    protected void serviceInit(Configuration conf) {
        List<Service> services = getServices();
        for (Service service : services) {
            service.init(conf);
        }
        super.serviceInit(conf);
    }

    protected void serviceStart() throws Exception {
        List<Service> services = getServices();
        for (Service service : services) {
            service.start();
        }
        super.serviceStart();
    }

    protected void serviceStop() throws Exception {
        List<Service> services = getServices();
        for (Service service : services) {
            service.stop();
        }
        super.serviceStart();
    }

}
