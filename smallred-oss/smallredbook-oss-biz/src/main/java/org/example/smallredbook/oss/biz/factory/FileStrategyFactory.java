package org.example.smallredbook.oss.biz.factory;

import org.example.smallredbook.oss.biz.strategy.FileStrategy;
import org.example.smallredbook.oss.biz.strategy.impl.AliyunOSSFileStrategy;
import org.example.smallredbook.oss.biz.strategy.impl.MinioFileStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: tzy
 * @Description: 通过工厂模式来隐藏文件上传的细节
 * @Date: 2024/8/11 17:34
 */
@Configuration
public class FileStrategyFactory {

    @Value("${storage.type}")
    private String strategyType;

    @Bean
    public FileStrategy getFileStrategy(){
        if(strategyType.equals("minio")){
            return new MinioFileStrategy();
        }else if(strategyType.equals("aliyun")){
            return new AliyunOSSFileStrategy();
        }

        throw new IllegalArgumentException("不可用的存储类型");
    }

}
