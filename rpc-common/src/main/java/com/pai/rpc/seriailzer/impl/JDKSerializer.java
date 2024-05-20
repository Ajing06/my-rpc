package com.pai.rpc.seriailzer.impl;

import com.pai.rpc.seriailzer.Serializer;

import java.io.*;

/**
 * 使用 JDK 自带的序列化方式
 * 这种方式只能序列化实现了 Serializable 接口的类，无法序列化Object类， 因此无法序列化我们的定义的RpcRequest和RpcResponse
 *
 */
@Deprecated
public class JDKSerializer implements Serializer {

    /**
     * 序列化
     * @param t
     * @return
     * @param <T>
     */
    public <T> byte[] serialize(T t) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(t);
            outputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 反序列化
     * @param bytes
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
            return (T) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
