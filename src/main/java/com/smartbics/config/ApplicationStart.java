package com.smartbics.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(value = "com.smartbics.booking")
public class ApplicationStart {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationStart.class, args);
    }
}
