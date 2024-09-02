package org.example.smallredbook.user.biz.service;

import jakarta.annotation.Resource;
import org.example.framework.common.response.Response;
import org.example.smallredbook.user.api.dto.req.FindUserByPhoneReqDTO;
import org.example.smallredbook.user.api.dto.req.RegisterUserReqDTO;
import org.example.smallredbook.user.api.dto.req.UpdateUserPasswordReqDTO;
import org.example.smallredbook.user.api.dto.resp.FindUserByPhoneRspDTO;
import org.example.smallredbook.user.biz.model.vo.UpdateUserInfoReqVO;

/**
 * @Author: tzy
 * @Description: 用户服务
 * @Date: 2024/8/14 14:09
 */
public interface UserService {

    /**
     * 修改用户信息
     * @param updateUserInfoReqVO
     * @return
     */
    Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO);

    /**
     * 用户注册
     * @param registerUserReqDTO
     * @return
     */
    Response<Long> register(RegisterUserReqDTO registerUserReqDTO);

    /**
     * 根据手机号查询用户信息
     * @param findUserByPhoneReqDTO
     * @return
     */
    Response<FindUserByPhoneRspDTO> findByPhone(FindUserByPhoneReqDTO findUserByPhoneReqDTO);

    /**
     * 更新密码
     *
     * @param updateUserPasswordReqDTO
     * @return
     */
    Response<?> updatePassword(UpdateUserPasswordReqDTO updateUserPasswordReqDTO);
}
