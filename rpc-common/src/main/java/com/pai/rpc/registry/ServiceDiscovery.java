package com.pai.rpc.registry;

import com.pai.rpc.entity.RpcRequest;

public interface ServiceDiscovery {

    String discovery(RpcRequest rpcRequest);
}
