package com.mb.tool.config;

import com.mb.tool.aspect.LogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(LogHandler.class)
public class LogConfig {
    @Bean
    @ConditionalOnMissingBean()
    public LogHandler logHandler() {
        return new LogHandler();
    }
}