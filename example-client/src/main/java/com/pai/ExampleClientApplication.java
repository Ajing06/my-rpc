package com.pai;

import com.pai.rpc.client.RpcProxy;
import com.pai.rpc.registry.ServiceDiscovery;
import com.pai.rpc.registry.zookeeper.ZookeeperServiceDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExampleClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleClientApplication.class, args);
    }

//    @Bean
//    public ServiceDiscovery serviceDiscovery() {
//        return new ZookeeperServiceDiscovery("47.108.208.206:2181");
//    }

    @Bean
    public RpcProxy rpcProxy(ServiceDiscovery serviceDiscovery) {
        return new RpcProxy(serviceDiscovery);
    }

}
