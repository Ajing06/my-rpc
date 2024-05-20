package com.pai.rpc.code;

import com.pai.rpc.seriailzer.impl.JDKSerializer;
import com.pai.rpc.seriailzer.impl.ProtostuffSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Decoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list){
        if(byteBuf.readableBytes() < 4){
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if(byteBuf.readableBytes() < dataLength){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] body = new byte[dataLength];
        byteBuf.readBytes(body);
        Object obj = new ProtostuffSerializer().deserialize(body, genericClass);
        //Object obj = ProtostuffSerializer.deserialize(body, genericClass);
        list.add(obj);
    }
}
