package com.pai;

import com.pai.rpc.client.RpcProxy;
import com.pai.rpc.client.RpcReference;
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

    @RpcReference(version = "2.0")
    private HelloService helloService2;

    @RpcReference(version = "3.0")
    private HelloService helloService;



    @GetMapping("/two")
    public String hello() {
        String s = helloService.hello("peter");
        return s;
    }

    @GetMapping("/three")
    public String hello2() {
        String hello = helloService2.hello("brain ");
//        String hello = rpcProxy.create(HelloService.class,"3.0").hello("3mz  ");
        System.out.println(hello);
        return hello;
    }
}
