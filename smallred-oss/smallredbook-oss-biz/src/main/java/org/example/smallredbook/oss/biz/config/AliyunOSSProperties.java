package org.example.smallredbook.oss.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: tzy
 * @Description:阿里云 OSS 配置项
 * @Date: 2024/8/13 15:37
 */
@ConfigurationProperties(prefix = "storage.aliyun-oss")
@Component
@Data
public class AliyunOSSProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}
