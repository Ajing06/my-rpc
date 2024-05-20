package com.pai.rpc.config;

import com.pai.rpc.seriailzer.Serializer;
import com.pai.rpc.seriailzer.impl.ProtostuffSerializer;
import com.sun.org.slf4j.internal.LoggerFactory;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SerializerConfig {

    private ApplicationContext applicationContext;

    private String serializerType;

    public SerializerConfig(@Value("${myrpc.serializer.type}") String serializerType,
                            ApplicationContext applicationContext){
        this.serializerType = serializerType;
        this.applicationContext = applicationContext;
    }

    @Bean
    public Serializer serializer(){
        if("protostuff".equals(serializerType)){
            log.info("Serializer: ProtostuffSerializer");
            return applicationContext.getBean(ProtostuffSerializer.class);
        } else {
            throw new IllegalArgumentException("Unsupported serializer type : " + serializerType + "!");
        }

    }

}
