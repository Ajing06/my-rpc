package com.pai.rpc.config;

import com.pai.rpc.registry.zookeeper.util.ZooKeeperUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    private final String zkAddress;

    public ZookeeperConfig(@Value("${myrpc.registry.address}") String zkAddress) {
        this.zkAddress = zkAddress;
    }

    @Bean
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkAddress)
                .connectionTimeoutMs(15000)
                .sessionTimeoutMs(60000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return client;
    }

    @Bean
//    @ConditionalOnMissingBean
    public ZooKeeperUtils zooKeeperTemplate(CuratorFramework client) {
        return new ZooKeeperUtils(client);
    }
}
