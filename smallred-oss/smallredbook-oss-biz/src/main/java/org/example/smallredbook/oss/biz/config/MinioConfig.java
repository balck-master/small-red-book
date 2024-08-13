package org.example.smallredbook.oss.biz.config;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: tzy
 * @Description: minio的配置
 * @Date: 2024/8/12 10:16
 */

@Configuration
public class MinioConfig {

    @Resource
    private MinioProperties minioProperties;
    @Bean
    public MinioClient minioClient(){
        // 构建 Minio 客户端
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
