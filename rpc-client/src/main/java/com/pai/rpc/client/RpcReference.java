package com.pai.rpc.client;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Component
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcReference {

    String version() default "";

}
