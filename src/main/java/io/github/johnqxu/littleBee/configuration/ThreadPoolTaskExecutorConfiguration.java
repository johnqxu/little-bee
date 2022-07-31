package io.github.johnqxu.littleBee.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadPoolTaskExecutorConfiguration {

    @Bean(name = "bee-executors")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(8);
        taskExecutor.setMaxPoolSize(16);
        taskExecutor.setQueueCapacity(10000);
        taskExecutor.setThreadNamePrefix("bee-");
        taskExecutor.initialize();
        return taskExecutor;
    }

}