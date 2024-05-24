package com.pai.rpc.loadbalance.impl;

import com.pai.rpc.entity.RpcRequest;
import com.pai.rpc.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        int i = ThreadLocalRandom.current().nextInt(serviceUrlList.size());
        return serviceUrlList.get(i);
    }
}
