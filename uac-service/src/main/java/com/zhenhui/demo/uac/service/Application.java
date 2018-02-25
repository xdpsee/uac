package com.zhenhui.demo.uac.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@MapperScan(basePackages = "com.zhenhui.demo.uac.core.repository.mybatis.mapper")
@ComponentScan(basePackages = "com.zhenhui.demo.uac")
@EnableCaching
@EnableAutoConfiguration
@ImportResource("classpath:druid/druid-stat-interceptor.xml")
@EnableAsync
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(128);
        executor.setThreadNamePrefix("Async-Exec-");
        executor.initialize();
        return executor;
    }
}


