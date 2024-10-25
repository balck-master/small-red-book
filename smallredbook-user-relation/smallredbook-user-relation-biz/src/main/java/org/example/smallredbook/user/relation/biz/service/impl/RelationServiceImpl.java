package org.example.smallredbook.user.relation.biz.service.impl;

import jakarta.annotation.Resource;
import org.example.framework.biz.context.holder.LoginUserContextHolder;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.example.smallredbook.user.api.dto.resp.FindUserByIdRspDTO;
import org.example.smallredbook.user.relation.biz.enums.ResponseCodeEnum;
import org.example.smallredbook.user.relation.biz.model.vo.FollowUserReqVO;
import org.example.smallredbook.user.relation.biz.rpc.UserRpcService;
import org.example.smallredbook.user.relation.biz.service.RelationService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/10/25 15:08
 */
public class RelationServiceImpl implements RelationService {
    @Resource
    private UserRpcService userRpcService;

    /**
     * 关注用户
     * @param followUserReqVO
     * @return
     */
    public Response<?> follow(FollowUserReqVO followUserReqVO) {
        //关注用户id
        Long followUserId = followUserReqVO.getFollowUserId();
        //当前登录用户id
        Long userId = LoginUserContextHolder.getUserId();

        //校验：不能关注自己
        if(Objects.equals(userId,followUserId)){
            throw new BizException(ResponseCodeEnum.CANT_FOLLOW_YOUR_SELF);
        }

        //  校验关注的用户是否存在
        FindUserByIdRspDTO findUserByIdRspDTO = userRpcService.findById(userId);
        if(!Objects.isNull(findUserByIdRspDTO)){
            throw new BizException(ResponseCodeEnum.FOLLOW_USER_NOT_EXISTED);
        }

        // TODO: 校验关注数是否已经达到上限

        // TODO: 写入 Redis ZSET 关注列表

        // TODO: 发送 MQ
        return Response.success();
    }
}
