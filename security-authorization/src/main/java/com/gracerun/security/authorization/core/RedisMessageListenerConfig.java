package com.gracerun.security.authorization.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * RedisSession配置
 *
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/6
 */
@Configuration
public class RedisMessageListenerConfig {

    public static final String PRIVILEGE_SYNC = "gracerun_privilege_sync";

    /**
     * 设置广播监听的通道
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   DynamicSecurityMetadataSource customSecurityMetadataSource) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(customSecurityMetadataSource, new ChannelTopic(RedisMessageListenerConfig.PRIVILEGE_SYNC));
        return container;
    }

}
