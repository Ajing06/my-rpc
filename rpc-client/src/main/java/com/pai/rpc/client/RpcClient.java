package com.pai.rpc.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 相当于自定义的自动依赖注入
 */

@Component
public class RpcClient implements BeanPostProcessor {

    private RpcProxy rpcProxy;

    public RpcClient(RpcProxy rpcProxy) {
        this.rpcProxy = rpcProxy;
    }

    /**
     *
     * 该方法在bean初始化之前后调用，实现自动依赖注入
     * 具体实现：
     *      1.获取bean的所有字段
     *      2.判断字段是否有RpcReference注解
     *      3.如果有，创建一个代理对象，然后注入到字段中
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference!=null) {
                String version = rpcReference.version();
                Object objectProxy = rpcProxy.create(field.getType(), version);
                field.setAccessible(true);
                try {
                    field.set(bean, objectProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
