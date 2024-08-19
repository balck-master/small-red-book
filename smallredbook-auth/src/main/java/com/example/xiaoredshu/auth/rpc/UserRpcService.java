package com.example.xiaoredshu.auth.rpc;

import com.example.xiaoredshu.auth.service.UserService;
import jakarta.annotation.Resource;
import org.example.framework.common.response.Response;
import org.example.smallredbook.user.api.api.UserFeignApi;
import org.example.smallredbook.user.api.dto.req.RegisterUserReqDTO;
import org.springframework.stereotype.Component;

/**
 * @Author: tzy
 * @Description:  用户服务远程调用
 * @Date: 2024/8/19 16:02
 */

@Component
public class UserRpcService {
    @Resource
    private UserFeignApi userFeignApi;

    /**
     * 用户注册
     * @param phone
     * @return
     */
    public Long registerUser(String phone){
        RegisterUserReqDTO registerUserReqDTO = new RegisterUserReqDTO();
        registerUserReqDTO.setPhone(phone);

        Response<Long> response = userFeignApi.registerUser(registerUserReqDTO);
        if(!response.isSuccess()){
            return null;
        }
        return response.getData();
    }
}
