package org.example.smallredbook.user.biz.service;

import jakarta.annotation.Resource;
import org.example.framework.common.response.Response;
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
}
