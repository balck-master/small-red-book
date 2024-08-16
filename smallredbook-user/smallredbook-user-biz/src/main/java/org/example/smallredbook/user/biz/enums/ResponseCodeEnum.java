package org.example.smallredbook.user.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.framework.common.exception.BaseExceptionInterface;

@AllArgsConstructor
@Getter
public enum ResponseCodeEnum implements BaseExceptionInterface {

    SYSTEM_ERROR("USER-10000","出错啦，后台小哥正在努力修复中"),
    PARAM_NOT_VALID("USER-10001","参数错误"),


    // ----------- 业务异常状态码 -----------
    NICK_NAME_VALID_FAIL("USER-20001", "昵称请设置2-24个字符，不能使用@《/等特殊字符"),
    XIAOHASHU_ID_VALID_FAIL("USER-20002", "小哈书号请设置6-15个字符，仅可使用英文（必须）、数字、下划线"),
    SEX_VALID_FAIL("USER-20003", "性别错误"),
    INTRODUCTION_VALID_FAIL("USER-20004", "个人简介请设置1-100个字符"),
    UPLOAD_AVATAR_FAIL("USER-20005", "头像上传失败"),
    UPLOAD_BACKGROUND_IMG_FAIL("USER-20006", "背景图上传失败"),
    ;




    // 异常码
    private final String errorCode;
    // 错误信息
    private final String errorMessage;

}
