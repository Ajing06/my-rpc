package com.pai.rpc;

import com.pai.rpc.entity.Dog;
import com.pai.rpc.entity.RpcRequest;
import com.pai.rpc.seriailzer.impl.JDKSerializer;
import com.pai.rpc.seriailzer.impl.ProtostuffSerializer;

public class Test {
    public static void main(String[] args) {

//        Object o = new Object();
//
//        byte[] bytes = new ProtostuffSerializer().serialize(o);
//        Object o1 = new ProtostuffSerializer().deserialize(bytes, Object.class);
//        System.out.println(o1);


//        Dog dog = new Dog("旺财", 3);
//        byte[] serialize = new ProtostuffSerializer().serialize(dog);
//        Dog dog1 =new ProtostuffSerializer().deserialize(serialize, Dog.class);
//        System.out.println(dog1.getName());
//
//
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId("123");
        rpcRequest.setInterfaceName("com.pai.rpc.service.HelloService");
        rpcRequest.setServiceVersion("1.0");
        rpcRequest.setMethodName("hello");
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setParameters(new Object[]{"张三"});
        byte[] serialize1 = new ProtostuffSerializer().serialize(rpcRequest);
        RpcRequest deserialize = new ProtostuffSerializer().deserialize(serialize1, RpcRequest.class);
//        byte[] serialize1 = ProtostuffSerializer.serialize(rpcRequest);
//
//        RpcRequest deserialize = ProtostuffSerializer.deserialize(serialize1, RpcRequest.class);

        System.out.println(deserialize);
    }
}
