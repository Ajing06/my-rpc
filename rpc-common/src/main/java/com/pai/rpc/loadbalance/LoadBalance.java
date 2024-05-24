package com.pai.rpc.loadbalance;


import com.pai.rpc.entity.RpcRequest;

import java.util.List;

public interface LoadBalance {

    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);
}
