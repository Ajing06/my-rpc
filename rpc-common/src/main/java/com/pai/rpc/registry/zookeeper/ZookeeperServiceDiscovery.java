package com.pai.rpc.registry.zookeeper;

import com.pai.rpc.entity.RpcRequest;
import com.pai.rpc.loadbalance.LoadBalance;
import com.pai.rpc.registry.ServiceDiscovery;
import com.pai.rpc.registry.zookeeper.util.ZooKeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private final ZooKeeperUtils zooKeeperUtils;

    private final String servicePath;

    private final LoadBalance loadBalance;

    public ZookeeperServiceDiscovery(ZooKeeperUtils zooKeeperUtils,
                                     @Value("${myrpc.registry.root-path}")String servicePath, LoadBalance loadBalance) {
        this.servicePath = servicePath;
        this.zooKeeperUtils = zooKeeperUtils;
        this.loadBalance = loadBalance;
    }

    @Override
    public String discovery(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getInterfaceName() + "-" + rpcRequest.getServiceVersion();
        if (!zooKeeperUtils.exists(servicePath, serviceName)) {
            throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
        }
        String path1 = servicePath + "/" + serviceName;
        List<String> children = zooKeeperUtils.getChildren(path1);
        if (children == null || children.isEmpty()) {
            throw new RuntimeException(String.format("can not find any address node on path: %s", path1));
        }

        //实现负载均衡
        String address = loadBalance.selectServiceAddress(children, rpcRequest);
        path1 = path1 + "/" + address;
        String data = zooKeeperUtils.getData(path1);
        log.info("discover service: {} from {}", serviceName, data);
        return data;
    }
}
