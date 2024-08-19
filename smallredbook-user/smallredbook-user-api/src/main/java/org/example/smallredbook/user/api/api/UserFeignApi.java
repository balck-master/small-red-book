package org.example.smallredbook.user.api.api;

import org.example.framework.common.response.Response;
import org.example.smallredbook.user.api.constant.ApiConstants;
import org.example.smallredbook.user.api.dto.req.RegisterUserReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: tzy
 * @Description: 用户远程调用接口
 * @Date: 2024/8/19 15:47
 */
@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface UserFeignApi {
    String PREFIX = "/user";

    /**
     * 用户注册
     * @param registerUserReqDTO
     * @return
     */
    @PostMapping(value = PREFIX + "/register")
    Response<Long> registerUser(@RequestBody  RegisterUserReqDTO registerUserReqDTO);
}
