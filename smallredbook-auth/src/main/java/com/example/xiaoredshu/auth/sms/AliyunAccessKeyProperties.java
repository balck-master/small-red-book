package com.example.xiaoredshu.auth.sms;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: tzy
 * @Description: 阿里云配置类：用于接收配置文件中填写的AccessKey信息
 * @Date: 2024/7/13 22:50
 */

@ConfigurationProperties(prefix = "aliyun")
@Component
@Data
public class AliyunAccessKeyProperties {

    private String accessKeyId;
    private String accessKeySecret;
}
