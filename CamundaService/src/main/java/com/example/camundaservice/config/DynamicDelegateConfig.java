package com.example.camundaservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import groovy.lang.GroovyClassLoader;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DynamicDelegateConfig {

    @Bean
    public GroovyClassLoader groovyClassLoader() {
        return new GroovyClassLoader();
    }
    
}
