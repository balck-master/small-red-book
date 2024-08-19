package org.example.smallredbook.user.api.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.framework.common.validator.PhoneNumber;

/**
 * @Author: tzy
 * @Description: 根据用户手机号来查询用户信息
 * @Date: 2024/8/19 16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserByPhoneReqDTO {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @PhoneNumber
    private String phone;
}
