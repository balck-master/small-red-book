package org.example.smallredbook.user.relation.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户取关 请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnfollowUserReqVO {

    @NotNull(message = "被取关用户 ID 不能为空")
    private Long unfollowUserId;
}