package com.pai.rpc.seriailzer.impl;

import com.pai.rpc.seriailzer.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.stereotype.Component;

/**
 * Protostuff 序列化器
 */
@Component
public class ProtostuffSerializer implements Serializer {

    /**
     * 使用Protostuff的LinkedBuffer类来分配一个默认大小的缓冲区，并将其设置为静态和最终的，
     * 意味着这个缓冲区在类加载时只会被分配一次，并且之后不会被重新分配或修改。
     * 这个缓冲区在序列化和反序列化过程中可能会被重复使用，以提高性能。
     */
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);


    public <T> byte[] serialize(T t) {
        Class<?> clazz = t.getClass();
        //获取该类的模式（Schema）。这个模式描述了如何序列化/反序列化该类的对象。
        Schema schema = RuntimeSchema.getSchema(clazz);
        byte[] bytes;
        try {
            //将对象序列化为字节数组，并将结果存储在`bytes`变量中。这个方法使用了之前定义的`BUFFER`作为缓冲区。
            bytes = ProtostuffIOUtil.toByteArray(t, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }
        return bytes;
    }


    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        //获取传入字节数组对应的类的模式（Schema）
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        //使用模式的newMessage()方法创建一个新的对象实例（这个对象是一个“空白”的实例，等待被填充数据）
        T obj = schema.newMessage();
        //将字节数组中的数据填充到该对象中
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }
}
