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

    @GetMapping("/hello")
    public void hello() {
        String hello = rpcProxy.create(HelloService.class,"1.0").hello("蔡徐坤  ");
        System.out.println(hello);
    }
}
