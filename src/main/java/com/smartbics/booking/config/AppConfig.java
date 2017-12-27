package com.smartbics.booking.config;

import com.smartbics.booking.dto.output.OutputFormatDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

    @Bean
    public OutputFormatDto outputInitializer() {
        OutputFormatDto outputFormatDto = new OutputFormatDto();
        return outputFormatDto;
    }
}
