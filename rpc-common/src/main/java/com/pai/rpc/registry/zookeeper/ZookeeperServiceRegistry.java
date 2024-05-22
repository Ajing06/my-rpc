package com.pai.rpc.registry.zookeeper;

import com.pai.rpc.registry.ServiceRegistry;
import com.pai.rpc.registry.zookeeper.util.ZooKeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private final ZooKeeperUtils zooKeeperUtils;

    private final String registryPath;

    public ZookeeperServiceRegistry(ZooKeeperUtils zooKeeperUtils,
                                    @Value("${myrpc.registry.root-path}") String registryPath) {
        this.zooKeeperUtils = zooKeeperUtils;
        this.registryPath = registryPath;
        log.info("-------------------connect zookeeper success---------------------");
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        String addressPath = registryPath + "/" + serviceName + "/address-";
        zooKeeperUtils.createNodeAndValue(addressPath, serviceAddress, CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info("create address node: {}", addressPath);
    }
}
