package com.smartbics.booking.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableAutoConfiguration
@SpringBootTest(classes = {
        WebTestConfig.class
})
@ComponentScan(value = "com.smartbics.booking")
public @interface WebTest {
}
