package org.example.smallredbook.user.relation.biz.config;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: tzy
 * @Description: 关注、取关MQ消费者限流器 配置类
 * @Date: 2024/10/26 14:42
 */
@Configuration
@RefreshScope
public class FollowUnfollowMqConsumerRateLimitConfig {

    @Value("${mq-consumer.follow-unfollow.rate-limit}")
    private double rateLimit;

    @Bean
    @RefreshScope
    public RateLimiter rateLimiter(){
        return RateLimiter.create(rateLimit);
    }
}
