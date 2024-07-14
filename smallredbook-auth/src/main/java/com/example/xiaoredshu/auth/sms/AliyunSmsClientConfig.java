package com.example.xiaoredshu.auth.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: tzy
 * @Description: aliyun短信发送工具类
 * @Date: 2024/7/13 22:54
 */

@Configuration
@Slf4j
public class AliyunSmsClientConfig {
    @Resource
    private AliyunAccessKeyProperties aliyunAccessKeyProperties;


    /**
     * <p>使用AK&amp;SK初始化账号Client</p>
     * @return Client
     */
    @Bean
    public Client smsClient(){
        Config config = new Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(aliyunAccessKeyProperties.getAccessKeyId())
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(aliyunAccessKeyProperties.getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        try {
            return new Client(config);
        } catch (Exception e) {
            log.error("初始化阿里云短信发送客户端错误:{}",e);
            return null;
        }
    }
}
