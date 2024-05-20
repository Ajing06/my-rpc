package com.pai.rpc.code;

import com.pai.rpc.seriailzer.impl.ProtostuffSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class Encoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public Encoder(Class<?> genericClass){
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if(genericClass.isInstance(o)){
            ProtostuffSerializer se = new ProtostuffSerializer();
            byte[] data = se.serialize(o);
            //byte[] data = ProtostuffSerializer.serialize(o);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
