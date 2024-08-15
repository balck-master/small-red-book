package org.example.smallredbook.user.biz.controller;

import jakarta.annotation.Resource;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.common.response.Response;
import org.example.smallredbook.user.biz.model.vo.UpdateUserInfoReqVO;
import org.example.smallredbook.user.biz.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: tzy
 * @Description: 用户
 * @Date: 2024/8/14 15:22
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping(value = "/update" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<?> updateUserInfo(@Validated UpdateUserInfoReqVO updateUserInfoReqVO){
        return userService.updateUserInfo(updateUserInfoReqVO);
    }
}
