package com.pai.rpc.client;

import com.pai.rpc.entity.RpcRequest;
import com.pai.rpc.entity.RpcResponse;
import com.pai.rpc.registry.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RpcProxy {

    private final ServiceDiscovery serviceDiscovery;

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(Class<T> clazz) {
        return create(clazz, "");
    }

    public <T> T create(Class<T> clazz, String serviceVersion) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setClassLoader(clazz.getClassLoader());
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setRequestId(UUID.randomUUID().toString());
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setParameterTypes(method.getParameterTypes());
            rpcRequest.setParameters(objects);
            rpcRequest.setInterfaceName(clazz.getName());
            rpcRequest.setServiceVersion(serviceVersion);

            String serviceAddress = null;

            if(serviceDiscovery != null) {
                String serviceName = clazz.getName();
                if(StringUtils.isNotBlank(serviceVersion)) {
                    serviceName += "-" + serviceVersion.trim();
                }

                serviceAddress = serviceDiscovery.discovery(serviceName);
                log.info("discover service: {} from {}", serviceName, serviceAddress);
            }

            if (StringUtils.isBlank(serviceAddress)) {
                throw new RuntimeException("service address is empty");
            }

            String[] split = serviceAddress.split(":");
            String host = split[0];
            int port = Integer.parseInt(split[1]);

            RpcClientHandler rpcClient = new RpcClientHandler(host, port);
            RpcResponse rpcResponse = rpcClient.send(rpcRequest);
            if (rpcResponse == null) {
                throw new RuntimeException("response is null");
            }
            if(rpcResponse.getException() != null) {
                throw rpcResponse.getException();
            } else {
                return rpcResponse.getResult();
            }
        });

        return (T) enhancer.create();
    }
}
