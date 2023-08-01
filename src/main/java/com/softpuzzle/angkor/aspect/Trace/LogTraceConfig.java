package com.softpuzzle.angkor.aspect.Trace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {    // 인스턴스가 딱 하나만 등록됨
        return new ThreadLocalLogTrace();
    }
}