package com.pai.rpc.loadbalance.impl;

import com.pai.rpc.entity.RpcRequest;
import com.pai.rpc.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private final AtomicInteger currentIndex = new AtomicInteger(0);
    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {

        int index = currentIndex.getAndUpdate(i -> (i + 1) % serviceUrlList.size());
        return serviceUrlList.get(index);
    }
}
