package org.example.smallredbook.oss.biz.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: tzy
 * @Description:  文件策略接口
 * @Date: 2024/8/11 17:21
 */
public interface FileStrategy {

    /**
     * 文件上传接口
     * @param multipartFile 文件
     * @param bucketName 桶名称
     * @return
     */
    String uploadFile(MultipartFile multipartFile , String bucketName);
}
