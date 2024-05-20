package com.pai.rpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    Class<?> interfaceName();

    String serviceVersion() default "";
}
