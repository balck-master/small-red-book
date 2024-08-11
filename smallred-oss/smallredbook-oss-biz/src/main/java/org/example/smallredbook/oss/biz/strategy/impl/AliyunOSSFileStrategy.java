package org.example.smallredbook.oss.biz.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.smallredbook.oss.biz.strategy.FileStrategy;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: tzy
 * @Description: 上传文件到阿里云oss
 * @Date: 2024/8/11 17:32
 */
@Slf4j
public class AliyunOSSFileStrategy implements FileStrategy {
    @Override
    public String uploadFile(MultipartFile multipartFile, String bucketName) {
        log.info("## 上传文件至 Aliyun ...");
        return null;
    }
}
