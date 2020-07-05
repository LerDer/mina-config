package com.lww.mina.config;

import javax.annotation.Resource;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lww
 * @date 2020-07-05 16:13
 */
@Configuration
@EnableConfigurationProperties(MinaServerProperty.class)
public class MinaServerConfig {

    @Resource
    private MinaServerProperty config;

    /**
     * 配置mina的多线程过滤器
     */
    @Bean
    public ExecutorFilter executorFilter() {
        //设置初始化线程数，最大线程数
        return new ExecutorFilter(config.getCorePoolSize(), config.getMaximumPoolSize());
    }

    /**
     * 配置mina的日志过滤器
     */
    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }
}
