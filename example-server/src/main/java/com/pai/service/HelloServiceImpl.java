package com.pai.service;

import com.pai.rpc.server.RpcService;

@RpcService(interfaceName = HelloService.class, serviceVersion = "1.0")
public class HelloServiceImpl implements HelloService {


    public String hello(String name) {
        return "Hello, " + name + "鸡你太美！";
    }


}
