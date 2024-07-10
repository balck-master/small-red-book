package com.example.xiaoredshu.auth.controller;

import com.example.xiaoredshu.auth.domain.dataobject.UserDO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.example.framework.biz.operationlog.aspect.ApiOperationLog;
import org.example.framework.common.response.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public Response<UserDO> test2(@RequestBody @Validated UserDO userDO){
        int i = 1/0;
        return Response.success();
    }

}
