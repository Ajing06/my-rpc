package com.pai.rpc.registry.zookeeper;

import com.pai.rpc.registry.ServiceDiscovery;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private String zkAddress;


    public ZookeeperServiceDiscovery(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    @Override
    public String discovery(String serviceName) {
        ZkClient zkClient = new ZkClient(zkAddress, 5000, 20000);

        try {
            String servicePath = "/registry/" + serviceName;
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> children = zkClient.getChildren(servicePath);
            if (children == null || children.size() == 0) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
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
            return zkClient.readData(servicePath + "/" + address);
        } finally {
            zkClient.close();
        }
    }
}
