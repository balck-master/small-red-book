package org.example.smallredbook.user.relation.biz.service;

import org.example.framework.common.response.Response;
import org.example.smallredbook.user.relation.biz.model.vo.FollowUserReqVO;

/**
 * @Author: tzy
 * @Description: 关注业务
 * @Date: 2024/10/25 15:07
 */
public interface RelationService {
    /**
     * 关注用户
     * @param followUserReqVO
     * @return
     */
    Response<?> follow(FollowUserReqVO followUserReqVO);
}
