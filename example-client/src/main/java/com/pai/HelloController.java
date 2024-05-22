package com.pai;

import com.pai.rpc.client.RpcProxy;
import com.pai.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/say")
public class HelloController {

    @Autowired
    private RpcProxy rpcProxy;

    @GetMapping("/two")
    public String hello() {
        String hello = rpcProxy.create(HelloService.class,"2.0").hello("peter  ");
        System.out.println(hello);
        return hello;
    }

    @GetMapping("/three")
    public String hello2() {
        String hello = rpcProxy.create(HelloService.class,"3.0").hello("3mz  ");
        System.out.println(hello);
        return hello;
    }
}
