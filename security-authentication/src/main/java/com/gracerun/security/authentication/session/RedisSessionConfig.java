package com.gracerun.security.authentication.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracerun.security.authentication.constant.LoginConstant;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.jackson2.SecurityJackson2Modules;
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
public class RedisSessionConfig implements BeanClassLoaderAware {

    private ClassLoader loader;

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
        return new GenericJackson2JsonRedisSerializer(mapper);
    }

    @Bean
    public HttpSessionIdResolver httpSessionStrategy() {
        MultiHttpSessionIdResolver multiHttpSessionIdResolver = new MultiHttpSessionIdResolver();
        multiHttpSessionIdResolver.add(new HeaderHttpSessionIdResolver(LoginConstant.HEADER_X_AUTH_TOKEN));
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

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }

}
