package org.example.framework.biz.context.config;

import org.example.framework.biz.context.interceptor.FeignRequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @Author: tzy
 * @Description: Feign 请求拦截器自动配置
 * @Date: 2024/8/16 15:39
 */
@AutoConfiguration
public class FeignContextAutoConfiguration {

    @Bean//将拦截器作为bean自动注入到 Spring 容器中
    public FeignRequestInterceptor feignRequestInterceptor(){
        return new FeignRequestInterceptor();
    }
}
