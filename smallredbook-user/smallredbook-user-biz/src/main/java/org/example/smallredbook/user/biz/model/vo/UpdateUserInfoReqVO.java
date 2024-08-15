package org.example.smallredbook.user.biz.model.vo;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * @Author: tzy
 * @Description: 更新用户信息 请求入参
 * @Date: 2024/8/14 10:37
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoReqVO {
//    由于用户修改信息时，可能只修改某一项，所以单独在业务层进行条件判断，而不是每个字段都添加校验注解。
    /**
     * 头像
     */
    private MultipartFile avater;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 小哈书ID
     */
    private String xiaohashuId;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 背景图
     */
    private MultipartFile  backgroundImg;

    /**
     * 个人介绍
     */
    private String introduction;

}
