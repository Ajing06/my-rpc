package com.pai.rpc.server;

import com.pai.rpc.entity.RpcRequest;
import com.pai.rpc.entity.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    // 服务名和服务对象的映射
    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());

        try {
            // 根据 rpcRequest 的内容，调用相应的方法
            Object result = handle(rpcRequest);
            response.setResult(result);
        } catch (Exception e) {
            response.setException(e);
        }
        // 写入 RPC 响应对象并自动关闭连接
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RpcRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 1、获取服务对象
        // 1.1 获取服务名称
        String interfaceName = request.getInterfaceName();
        // 1.2 获取服务版本
        String serviceVersion = request.getServiceVersion();
        if (serviceVersion != null && !"".equals(serviceVersion)) {
            interfaceName += "-" + serviceVersion;
        }
        // 1.3 获取服务对象
        Object serviceBean = handlerMap.get(interfaceName);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("can not find service bean by key: %s", interfaceName));
        }
        // 2、反射调用方法，返回执行结果
        Class<?> clazz = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        Method method = clazz.getMethod(methodName, parameterTypes);
        //若方法为private，则设置为可访问
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);

    }
}
