package com.golov.springspace.simplecontroller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackages = {"randomizer", "loader", "output", "input", "com.golov.springspace", "parser"})
@EnableAsync
@EnableScheduling
public class RestConfiguration implements AsyncConfigurer {
    @Bean
    public TaskExecutor taskExecutor() {
        return new ConcurrentTaskExecutor(Executors.newCachedThreadPool());
//        return Executors.newCachedThreadPool(); //
    }

}

