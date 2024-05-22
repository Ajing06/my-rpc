package com.pai.service;

import com.pai.rpc.server.RpcService;

@RpcService(interfaceName = HelloService.class, serviceVersion = "3.0")
public class HelloServiceImpl2 implements HelloService {


    public String hello(String name) {
        return "Hello, " + name + " APEX 是全天下最好玩的游戏！";
    }


}
