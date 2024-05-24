package com.pai.rpc.config;

import com.pai.rpc.loadbalance.LoadBalance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ServiceLoader;

@Configuration
public class LoadBalanceFactory {

    @Value("${myrpc.loadbalance.type}")
    private String LoadBalanceType;

    @Bean
    public LoadBalance loadBalance(){
        ServiceLoader<LoadBalance> load = ServiceLoader.load(LoadBalance.class);
        for (LoadBalance loadBalance : load) {
            if(loadBalance.getClass().getName().equals(LoadBalanceType)){
                return loadBalance;
            }
        }
        throw new IllegalArgumentException("Unsupported load balance type : " + LoadBalanceType + "!");
    }
}
