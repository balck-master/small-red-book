package com.example.xiaoredshu.auth.controller;

import lombok.extern.slf4j.Slf4j;

import org.example.framework.biz.operationlog.aspect.ApiOperationLog;
import org.example.framework.common.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/4 22:50
 */

@RestController
@Slf4j
public class TestController {

    @ApiOperationLog(description = "测试接口")
    @GetMapping("/test")
    public Response<String> test(){
        return Response.success("hello 黑大帅");
    }


    @GetMapping("/test2")
    @ApiOperationLog(description = "测试接口2")
    public Response<User> test2(){
        return Response.success( User.builder()
                .nickName("黑大帅")
                .createTime(LocalDateTime.now())
                .build());
    }

}
