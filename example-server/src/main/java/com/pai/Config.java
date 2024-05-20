package com.pai;

import com.pai.rpc.registry.ServiceRegistry;
import com.pai.rpc.registry.zookeeper.ZookeeperServiceRegistry;
import com.pai.rpc.server.RpcServer;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${myrpc.registry.address}")
    private String zkAddress;

    @Value("${myrpc.server.address}")
    private String serviceAddress;

    @Bean
    public ServiceRegistry serviceRegistry(){
        ZkClient zkClient = new ZkClient(zkAddress, 5000, 20000);
        return new ZookeeperServiceRegistry(zkClient);
    }

    @Bean
    public RpcServer rpcServer(ServiceRegistry serviceRegistry) {
        return new RpcServer(serviceAddress, serviceRegistry);
    }

}
