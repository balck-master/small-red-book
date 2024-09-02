package com.example.xiaoredshu.auth.controller;

import com.example.xiaoredshu.auth.model.vo.user.UpdatePasswordReqVO;
import com.example.xiaoredshu.auth.model.vo.user.UserLoginReqVO;
import com.example.xiaoredshu.auth.service.AuthService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.biz.operationlog.aspect.ApiOperationLog;
import org.example.framework.common.response.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/17 14:41
 */

@RestController
@Slf4j
//@RequestMapping("/user")
public class AuthController {

    @Resource
    private AuthService authService;

    @GetMapping("/login")
    @ApiOperationLog(description = "用户登录/注册")
    public Response<String> loginAndRegister(@Validated @RequestBody UserLoginReqVO userLoginReqVO){
        return authService.loginAndRegister(userLoginReqVO);
    }

    @PostMapping("/logout")
    @ApiOperationLog(description = "用户登出")
    public Response<?> logout(@RequestHeader("userId") String userId){
        // todo 账号退出登录逻辑待实现
        authService.logout();
        return Response.success();
    }

    /**
     * 修改密码
     * @param updatePasswordReqVO
     * @return
     */
    @PostMapping("/password/update")
    @ApiOperationLog(description = "修改密码")
    public Response<?> updatePassword(@Validated @RequestBody UpdatePasswordReqVO updatePasswordReqVO){
        return  authService.updatePassword(updatePasswordReqVO);
    }



}
