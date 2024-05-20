package com.pai.rpc.registry.zookeeper;

import com.pai.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

@Slf4j
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private ZkClient zkClient;

    public ZookeeperServiceRegistry(ZkClient zkClient) {
        this.zkClient = zkClient;
        log.info("connect zookeeper");
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        String registryPath = "/registry";
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            log.info("create registry node: {}", registryPath);
        }
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            log.info("create service node: {}", servicePath);
        }
        String addressPath = servicePath + "/address-";
        zkClient.createEphemeralSequential(addressPath, serviceAddress);
        log.info("create address node: {}", addressPath);
    }
}
