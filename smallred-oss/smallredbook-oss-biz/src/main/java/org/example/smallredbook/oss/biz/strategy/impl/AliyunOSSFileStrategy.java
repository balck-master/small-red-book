package org.example.smallredbook.oss.biz.strategy.impl;

import com.aliyun.oss.OSS;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.smallredbook.oss.biz.config.AliyunOSSProperties;
import org.example.smallredbook.oss.biz.strategy.FileStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.UUID;

/**
 * @Author: tzy
 * @Description: 上传文件到阿里云oss
 * @Date: 2024/8/11 17:32
 */
@Slf4j
public class AliyunOSSFileStrategy implements FileStrategy {
    @Resource
    private AliyunOSSProperties aliyunOSSProperties;
    @Resource
    private OSS ossClient;

    @SneakyThrows
    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info("## 上传文件至 阿里云 OSS ...");

        //判断文件是否为空
        if (file == null || file.getSize() == 0){
            log.error("==> 上传文件异常：文件大小为空");
            throw new RuntimeException("文件大小为空...");
        }

        //文件原始名称 ,文件类型
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        // 生成存储对象的名称（将 UUID 字符串中的 - 替换成空字符串）
        String key = UUID.randomUUID().toString().replace("-", "");
        // 获取文件的后缀 ，如 .jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //拼接文件后缀，即为要存储的文件名
        String objectName = String.format("%s%s", key, suffix);

        //上传文件至阿里云OSS
        ossClient.putObject(bucketName,objectName,new ByteArrayInputStream(file.getInputStream().readAllBytes()));

        //返回文件的访问链接
        String url =  String.format("https://%s.%s/%s",bucketName,aliyunOSSProperties.getEndpoint(),objectName);
        log.info("==> 上传文件至阿里云 OSS 成功，访问路径: {}", url);
        return url;
    }
}
