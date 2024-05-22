package com.pai.rpc.server;

import com.pai.rpc.code.Decoder;
import com.pai.rpc.code.Encoder;
import com.pai.rpc.entity.RpcRequest;
import com.pai.rpc.entity.RpcResponse;
import com.pai.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RpcServer implements ApplicationContextAware, InitializingBean {

    private final String serviceAddress;
    private final ServiceRegistry serviceRegistry;


    public RpcServer(@Value("${myrpc.server.address}") String serviceAddress, ServiceRegistry serviceRegistry) {
        this.serviceAddress = serviceAddress;
        this.serviceRegistry = serviceRegistry;
    }

    Map<String, Object> handlerMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);

        if (!beansWithAnnotation.isEmpty()) {
            for (Object bean : beansWithAnnotation.values()) {
                RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.interfaceName().getName();
                String serviceVersion = rpcService.serviceVersion();
                if (serviceVersion != null && !"".equals(serviceVersion)) {
                    serviceName += "-" + serviceVersion;
                }
                handlerMap.put(serviceName, bean);
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new Encoder(RpcResponse.class))
                                    .addLast(new Decoder(RpcRequest.class))
                                    .addLast(new RpcServerHandler(handlerMap));
                        }
                    });
            String[] split = serviceAddress.split(":");
            String ip = split[0];
            int port = Integer.parseInt(split[1]);
            ChannelFuture future = serverBootstrap.bind(ip, port).sync();

            // 注册服务
            if (serviceRegistry != null) {
                for (String interfaceName : handlerMap.keySet()) {
                    serviceRegistry.register(interfaceName, serviceAddress);
                    log.info("register service: {} => {}", interfaceName, serviceAddress);
                }
            }

            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
