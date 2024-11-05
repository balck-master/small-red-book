package org.example.smallredbook.oss.api;

import org.example.framework.common.response.Response;
import org.example.smallredbook.oss.config.FeignFormConfig;
import org.example.smallredbook.oss.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: tzy
 * @Description: 文件远程调用
 * @Date: 2024/8/15 14:54
 */
@FeignClient(name = ApiConstants.SERVICE_NAME  , configuration = FeignFormConfig.class)
public interface FileFeignApi {

    String PREFIX = "/file";

    @PostMapping(value = PREFIX + "/test")
    Response<?> test();

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping(value = PREFIX+"/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<?> uploadFile(@RequestPart(value = "file")MultipartFile file);



}
