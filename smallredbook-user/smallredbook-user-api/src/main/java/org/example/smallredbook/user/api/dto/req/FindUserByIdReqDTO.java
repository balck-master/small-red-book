package org.example.smallredbook.user.api.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: tzy
 * @Description: 根据用户 ID 查询用户信息 前端传来的
 * @Date: 2024/9/8 21:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserByIdReqDTO {
    /**
     * 手机号
     */
    @NotNull(message = "用户 ID 不能为空")
    private Long id;
}
