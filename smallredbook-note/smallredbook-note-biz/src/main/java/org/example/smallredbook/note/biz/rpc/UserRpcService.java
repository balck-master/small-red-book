package org.example.smallredbook.note.biz.rpc;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.example.framework.common.response.Response;
import org.example.smallredbook.note.biz.model.vo.FindNoteDetailReqVO;
import org.example.smallredbook.user.api.api.UserFeignApi;
import org.example.smallredbook.user.api.dto.req.FindUserByIdReqDTO;
import org.example.smallredbook.user.api.dto.resp.FindUserByIdRspDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

/**
 * @Author: tzy
 * @Description: 用户服务
 * @Date: 2024/9/10 20:24
 */
@Component
public class UserRpcService {
    @Resource
    private UserFeignApi userFeignApi;

    /**
     *
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    public FindUserByIdRspDTO findById(Long userId){
        FindUserByIdReqDTO findUserByIdReqDTO = new FindUserByIdReqDTO();
        findUserByIdReqDTO.setId(userId);

        Response<FindUserByIdRspDTO> response = userFeignApi.findById(findUserByIdReqDTO);
        if(Objects.isNull(response) || !response.isSuccess()){
            return null;
        }
        return response.getData();
    }
}
