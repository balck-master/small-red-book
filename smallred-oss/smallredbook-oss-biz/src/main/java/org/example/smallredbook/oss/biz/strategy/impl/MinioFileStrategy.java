package org.example.smallredbook.oss.biz.strategy.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.smallredbook.oss.biz.config.MinioProperties;
import org.example.smallredbook.oss.biz.strategy.FileStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

/**
 * @Author: tzy
 * @Description: 上传文件到Minio
 * @Date: 2024/8/11 17:31
 */
@Slf4j
public class MinioFileStrategy implements FileStrategy {

    @Resource
    private MinioClient minioClient;
    @Resource
    private MinioProperties minioProperties;
    @SneakyThrows
    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info("## 上传文件至 Minio ...");

        //判断文件是否为空
        if(file == null || file.getSize() == 0){
            log.error("==> 上传文件异常:文件大小不能为空...");
            throw new RuntimeException("文件大小不能为空");
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

        log.info("==》 开始上传文件至Minio ， ObjectName: {}" , objectName);

        minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream() , file.getSize() , -1)
                        .contentType(contentType)
                        .build());

        //返回文件的访问链接
        String url = String.format("%s/%s/%s", minioProperties.getEndpoint(), bucketName, objectName);
        log.info("==> 上传文件至 Minio 成功 ， 访问路径：{}", url);
        return url;
    }
}
