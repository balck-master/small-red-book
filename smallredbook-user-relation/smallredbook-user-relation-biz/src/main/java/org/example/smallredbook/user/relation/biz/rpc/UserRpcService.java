package org.example.smallredbook.user.relation.biz.rpc;

import org.example.framework.common.response.Response;
import org.example.smallredbook.user.api.api.UserFeignApi;
import org.example.smallredbook.user.api.dto.req.FindUserByIdReqDTO;
import org.example.smallredbook.user.api.dto.req.FindUserByPhoneReqDTO;
import org.example.smallredbook.user.api.dto.resp.FindUserByIdRspDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: tzy
 * @Description: 用户服务远程调用
 * @Date: 2024/10/25 15:21
 */
@Component
public class UserRpcService {

    private UserFeignApi userFeignApi;

    public FindUserByIdRspDTO findById(Long userId){
        FindUserByIdReqDTO findUserByIdReqDTO = new FindUserByIdReqDTO();
        findUserByIdReqDTO.setId(userId);

        Response<FindUserByIdRspDTO> response = userFeignApi.findById(findUserByIdReqDTO);
        if(!response.isSuccess() || Objects.isNull(response)){
            return null;
        }
        return response.getData();
    }
}
