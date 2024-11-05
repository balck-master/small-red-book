package org.example.smallredbook.oss.biz.service;

import org.example.framework.common.response.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/8/11 20:03
 */
public interface FileService {
    Response<?> uploadFile(MultipartFile multipartFile);
}
