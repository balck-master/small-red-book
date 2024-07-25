package com.example.xiaoredshu.auth.service;

import com.example.xiaoredshu.auth.model.vo.verificationcode.SendVerificationCodeReqVO;
import org.example.framework.common.response.Response;

/**
 * @Author: tzy
 * @Description: 验证码业务接口
 * @Date: 2024/7/13 18:04
 */
public interface VerificationCodeService {

    /**
     * 发送短信验证码
     * @param sendVerificationCodeReqVO
     * @return
     */
    Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO);
}
