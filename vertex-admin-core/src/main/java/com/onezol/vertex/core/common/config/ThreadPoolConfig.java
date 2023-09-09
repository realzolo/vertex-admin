package com.onezol.vertex.core.common.config;

import com.onezol.vertex.common.util.ThreadUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 **/
@Configuration
@SuppressWarnings("all")
public class ThreadPoolConfig {
    // 核心线程池大小
    private final int corePoolSize = 50;
    // 最大可创建的线程数
    private final int maxPoolSize = 200;
    // 队列最大长度
    private final int queueCapacity = 1000;
    // 线程池维护线程所允许的空闲时间
    private final int keepAliveSeconds = 300;

    @Value("${spring.application.name:vertex-admin-app}")
    private String applicationName;

    @Bean(name = "threadPoolTaskExecutor")  // 公共线程池
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(applicationName + "-thread-pool-");
        executor.setThreadGroupName(applicationName + "-thread-group");
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(corePoolSize, Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                ThreadUtils.printException(r, t);
            }
        };
    }
}
