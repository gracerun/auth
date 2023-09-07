package com.gracerun.security.authentication.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import java.util.concurrent.Executor;

/**
 * RedisSession配置
 *
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/6
 */
@Configuration
public class RedisSessionConfig {

    public static final String HEADER_X_AUTH_TOKEN = "x-auth-token";

    @Bean
    public HttpSessionIdResolver httpSessionStrategy() {
        MultiHttpSessionIdResolver multiHttpSessionIdResolver = new MultiHttpSessionIdResolver();
        multiHttpSessionIdResolver.add(new HeaderHttpSessionIdResolver(HEADER_X_AUTH_TOKEN));
        multiHttpSessionIdResolver.add(new CookieHttpSessionIdResolver());
        return multiHttpSessionIdResolver;
    }

    /**
     * sessionRedis线程池
     *
     * @return
     */
    @Bean("springSessionRedisTaskExecutor")
    public Executor springSessionRedisTaskExecutor() {
        ThreadPoolTaskExecutor springSessionRedisTaskExecutor = new ThreadPoolTaskExecutor();
        springSessionRedisTaskExecutor.setCorePoolSize(16);
        springSessionRedisTaskExecutor.setMaxPoolSize(16);
        springSessionRedisTaskExecutor.setKeepAliveSeconds(10);
        springSessionRedisTaskExecutor.setQueueCapacity(1000);
        springSessionRedisTaskExecutor.setThreadNamePrefix("Spring session redis executor thread: ");
        return springSessionRedisTaskExecutor;
    }

}
