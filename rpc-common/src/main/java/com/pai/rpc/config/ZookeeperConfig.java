package com.pai.rpc.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    private String zkAddress;

    public ZookeeperConfig(@Value("myrpc.registry.address") String zkAddress) {
        this.zkAddress = zkAddress;
    }

    @Bean
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(zkAddress, new ExponentialBackoffRetry(1000, 3));
    }
}
