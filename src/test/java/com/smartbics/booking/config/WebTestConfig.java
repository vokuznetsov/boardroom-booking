package com.smartbics.booking.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootConfiguration
public class WebTestConfig {

    @ConditionalOnMissingBean(MockMvc.class)
    @Bean
    public MockMvc mockMvc(WebApplicationContext context) {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(context)
                .defaultRequest(get("/")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .alwaysDo(print());

        return builder.build();
    }
}
