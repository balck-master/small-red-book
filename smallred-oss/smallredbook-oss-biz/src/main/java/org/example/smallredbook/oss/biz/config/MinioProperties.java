package org.example.smallredbook.oss.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: tzy
 * @Description: minio配置
 * @Date: 2024/8/11 22:08
 */

//@ConfigurationProperties:读取文件前缀为minio的配置
@ConfigurationProperties(prefix = "storage.minio")
@Component
@Data
public class MinioProperties {

    private String endpoint;
    private String accessKey;
    private String secretKey;
}
