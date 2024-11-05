package org.example.smallredbook.oss.biz.controller;

import kotlin.SinceKotlin;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.example.framework.biz.operationlog.aspect.ApiOperationLog;
import org.example.framework.common.response.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/8/15 11:13
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class TestFenginController {

    @PostMapping(value = "/test")
    @ApiOperationLog(description="测试接口")
    public Response<?> test(){
        return Response.success();
    }
}
