package ar.edu.um.fi.programacion2.reports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Configuration
public class RecurrReportThreadConfig {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();

        // Configuration for the thread pool
        threadPool.setPoolSize(5);
        threadPool.setThreadNamePrefix("ReporteRecurrenteScheduler-");
        threadPool.initialize();

        return threadPool;
    }
}
