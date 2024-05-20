package com.pai.rpc.client;

import com.pai.rpc.code.Decoder;
import com.pai.rpc.code.Encoder;
import com.pai.rpc.entity.RpcRequest;
import com.pai.rpc.entity.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private String host;
    private int port;

    private RpcResponse response;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse o) throws Exception {
        this.response = o;
    }

    public RpcResponse send(RpcRequest rpcRequest) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new Encoder(RpcRequest.class))
                                    .addLast(new Decoder(RpcResponse.class))
                                    .addLast(RpcClient.this);
                        }
                    });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            ChannelFuture future = bootstrap.connect(host, port).sync();

            future.channel().writeAndFlush(rpcRequest).sync();

            future.channel().closeFuture().sync();

            return response;
        } finally {
            group.shutdownGracefully();
        }
    }


}
