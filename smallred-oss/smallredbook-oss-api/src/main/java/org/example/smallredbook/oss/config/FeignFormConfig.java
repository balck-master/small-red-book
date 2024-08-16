package org.example.smallredbook.oss.config;

import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.Encoder;

/**
 * @Author: tzy
 * @Description: feign 表单配置
 * @Date: 2024/8/15 21:03
 */
@Configuration
public class FeignFormConfig {

    @Bean
    public SpringFormEncoder feignFormEncoder(){
        return new SpringFormEncoder();
    }
}
