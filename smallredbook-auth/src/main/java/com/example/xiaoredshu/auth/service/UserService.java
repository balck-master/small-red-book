package com.example.xiaoredshu.auth.service;

import com.example.xiaoredshu.auth.model.vo.user.UserLoginReqVO;
import org.example.framework.common.response.Response;
import org.springframework.stereotype.Service;

/**
 * @Author: tzy
 * @Description: 用户验证接口
 * @Date: 2024/7/16 21:20
 */
@Service
public interface UserService {

    /**
     * 用户登录与注册
     * @param userLoginReqVO
     * @return
     */
    Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO);

    /**
     * 注册用户
     * @param phone
     * @return
     */
    Long registerUser(String phone);

    /**
     * 用户登出
     * @param userId
     * @return
     */
    Response<?> logout();
}
