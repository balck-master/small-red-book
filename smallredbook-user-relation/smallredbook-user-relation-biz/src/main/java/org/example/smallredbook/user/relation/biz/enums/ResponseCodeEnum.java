package org.example.smallredbook.user.relation.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.framework.common.exception.BaseExceptionInterface;

/**
 * 响应异常码
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("RELATION-10000", "出错啦，后台小哥正在努力修复中..."),
    PARAM_NOT_VALID("RELATION-10001", "参数错误"),

    // ----------- 业务异常状态码 -----------
    CANT_FOLLOW_YOUR_SELF("RELATION-20001", "无法关注自己"),
    FOLLOW_USER_NOT_EXISTED("RELATION-20002", "关注的用户不存在"),
    ;
    ;

    // 异常码
    private final String errorCode;
    // 错误信息
    private final String errorMessage;

}
