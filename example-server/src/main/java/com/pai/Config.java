package com.pai;

import com.pai.rpc.registry.ServiceRegistry;
import com.pai.rpc.server.RpcServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${myrpc.server.address}")
    private String serviceAddress;

    @Bean
    public RpcServer rpcServer(ServiceRegistry serviceRegistry) {
        return new RpcServer(serviceAddress, serviceRegistry);
    }

}
