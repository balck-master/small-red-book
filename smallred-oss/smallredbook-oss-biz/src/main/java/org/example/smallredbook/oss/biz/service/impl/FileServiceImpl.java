package org.example.smallredbook.oss.biz.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.common.response.Response;
import org.example.smallredbook.oss.biz.service.FileService;
import org.example.smallredbook.oss.biz.strategy.FileStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: tzy
 * @Description: 文件实现类
 * @Date: 2024/8/11 20:04
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private FileStrategy fileStrategy;

    private static final String BUCKET_NAME = "xiaohashu";
    @Override
    public Response<?> uploadFile(MultipartFile multipartFile) {
        String url = fileStrategy.uploadFile(multipartFile, BUCKET_NAME);
        return Response.success(url);
    }
}
