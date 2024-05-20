package com.pai.rpc.seriailzer;

public interface Serializer {

    <T> byte[] serialize(T t);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
