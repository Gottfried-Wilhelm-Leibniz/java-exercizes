package com.golov.springspace.application;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = {"randomizer", "loader", "output", "input", "com.golov.springspace", "parser"})
@EnableAsync
@EnableScheduling
public class AppConfiguration implements AsyncConfigurer {

}

//@Bean
//    public TaskExecutor taskExecutor() {
//        return new ConcurrentTaskExecutor(Executors.newCachedThreadPool());
////        return Executors.newCachedThreadPool(); //
//    }