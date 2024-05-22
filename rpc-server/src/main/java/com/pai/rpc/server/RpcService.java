package com.pai.rpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  各注解的说明
 *  @Target(ElementType.TYPE)         表明该注解可以用在类、接口（包括注解类型）或enum声明上
 *  @Component                            表明该注解是一个组件类，Spring会自动扫描到该类并把它注册为Spring应用上下文中的一个bean。
 *  @Retention(RetentionPolicy.RUNTIME)   该注解在运行时仍然可见，可以通过反射来读取它。允许在运行时检查和操作注解。
 */

@Target(ElementType.TYPE)
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    Class<?> interfaceName();

    String serviceVersion() default "";
}
