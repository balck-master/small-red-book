package com.example.xiaoredshu.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.xiaoredshu.auth.domain.dataobject.UserDO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.example.framework.biz.operationlog.aspect.ApiOperationLog;
import org.example.framework.common.response.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        log.info(String.valueOf(LocalDateTime.now()));
        return Response.success("hello 黑大帅");
    }


    @GetMapping("/test2")
    @ApiOperationLog(description = "测试接口2")
    public Response<UserDO> test2(@RequestBody @Validated UserDO userDO){
        log.info(String.valueOf(LocalDateTime.now()));
//        int i = 1/0;
        return Response.success();
    }


    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @RequestMapping("/user/doLogin")
    public String doLogin(String username, String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            return "登录成功";
        }
        return "登录失败";
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @GetMapping("/user/isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

}
