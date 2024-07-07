package org.example.framework.biz.operationlog.config;

import lombok.extern.slf4j.Slf4j;
import org.example.framework.biz.operationlog.aspect.ApiOperationLogAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: tzy
 * @Description: 自动配置类，用于配置 API 操作日志记录功能，
 *              并且通过 @Bean 注解的 apiOperationLogAspect() 方法来创建一个 ApiOperationLogAspect 实例，
 *              以实现注入到 Spring 容器中。
 * @Date: 2024/7/5 11:21
 */
@Configuration
@Slf4j
public class ApiOperationLogAutoConfiguration {

    @Bean
    public ApiOperationLogAspect apiOperationLogAspect(){
        log.info("进入config");
        return new ApiOperationLogAspect();
    }
}