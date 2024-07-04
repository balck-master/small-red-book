package com.example.xiaoredshu.auth.controller;

import org.example.utils.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/4 22:50
 */

@RestController
public class TestController {

    @GetMapping("/test")
    public Response<String> test(){
        return Response.success("hello 黑大帅");
    }
}
