package com.pai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextAware;

@SpringBootTest
class ExampleClientApplicationTests {

    @Autowired
    private ApplicationContextAware applicationContextAware;

    @Test
    void contextLoads() {

    }

}
