package com.example.xiaoredshu.auth.service;

import com.example.xiaoredshu.auth.model.vo.user.UpdatePasswordReqVO;
import com.example.xiaoredshu.auth.model.vo.user.UserLoginReqVO;
import org.example.framework.common.response.Response;
import org.springframework.stereotype.Service;

/**
 * @Author: tzy
 * @Description: 用户验证接口
 * @Date: 2024/7/16 21:20
 */
@Service
public interface AuthService {

    /**
     * 用户登录与注册
     * @param userLoginReqVO
     * @return
     */
    Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO);


    /**
     * 用户登出
     * @param
     * @return
     */
    Response<?> logout();

    /**
     * 修改密码
     * @param updatePasswordReqVO
     * @return
     */
    Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO);
}
