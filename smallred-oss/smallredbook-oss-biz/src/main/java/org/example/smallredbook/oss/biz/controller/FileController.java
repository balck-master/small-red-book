package org.example.smallredbook.oss.biz.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.common.response.Response;
import org.example.smallredbook.oss.biz.service.FileService;
import org.example.smallredbook.oss.biz.service.impl.FileServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/8/11 20:06
 */

@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;

//    提交方式是 MULTIPART_FORM_DATA_VALUE, 代表此接口通过表单方式提交，而不是 JSON 方式，因为涉及到文件上传。
    @PostMapping(value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<?> uploadFile(@RequestPart(value = "file")MultipartFile file){
        return fileService.uploadFile(file);
    }
}
