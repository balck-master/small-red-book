package org.example.smallredbook.user.relation.biz.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.biz.operationlog.aspect.ApiOperationLog;
import org.example.framework.common.response.Response;
import org.example.smallredbook.user.relation.biz.model.vo.FollowUserReqVO;
import org.example.smallredbook.user.relation.biz.model.vo.UnfollowUserReqVO;
import org.example.smallredbook.user.relation.biz.service.RelationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: tzy
 * @Description: 用户关系
 * @Date: 2024/10/25 16:45
 */
@RestController
@Slf4j
@RequestMapping("/relation")
public class RelationController {

    @Resource
    private RelationService relationService;

    @PostMapping("/follow")
    @ApiOperationLog(description = "关注用户")
    public Response<?> follow(@Validated @RequestBody FollowUserReqVO followUserReqVO) {
        return relationService.follow(followUserReqVO);
    }

    @PostMapping("/unfollow")
    @ApiOperationLog(description = "取关用户")
    public Response<?> unfollow(@Validated @RequestBody UnfollowUserReqVO unfollowUserReqVO) {
        return relationService.unfollow(unfollowUserReqVO);
    }
}
