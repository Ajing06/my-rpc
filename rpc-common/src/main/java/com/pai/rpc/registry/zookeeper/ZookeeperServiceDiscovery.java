package com.pai.rpc.registry.zookeeper;

import com.pai.rpc.registry.ServiceDiscovery;
import com.pai.rpc.registry.zookeeper.util.ZooKeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private final ZooKeeperUtils zooKeeperUtils;

    private final String servicePath;

    public ZookeeperServiceDiscovery(ZooKeeperUtils zooKeeperUtils,
                                    @Value("${myrpc.registry.root-path}")String servicePath) {
        this.servicePath = servicePath;
        this.zooKeeperUtils = zooKeeperUtils;
    }

    @Override
    public String discovery(String serviceName) {

        if (!zooKeeperUtils.exists(servicePath, serviceName)) {
            throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
        }
        String path1 = servicePath + "/" + serviceName;
        List<String> children = zooKeeperUtils.getChildren(path1);
        if (children == null || children.isEmpty()) {
            throw new RuntimeException(String.format("can not find any address node on path: %s", path1));
        }
        int size = children.size();
        String address;
        if(size == 1) {
            address = children.get(0);
            log.info("get only address node: {}", address);
        } else {
            address = children.get(ThreadLocalRandom.current().nextInt(size));
            log.info("get random address node: {}", address);
        }
        path1 = path1 + "/" + address;
        return zooKeeperUtils.getData(path1);

    }
}
