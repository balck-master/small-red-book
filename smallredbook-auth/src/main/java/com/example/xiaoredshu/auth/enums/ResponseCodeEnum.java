package com.example.xiaoredshu.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.framework.common.exception.BaseExceptionInterface;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/9 20:09
 */

@AllArgsConstructor
@Getter
public enum ResponseCodeEnum implements BaseExceptionInterface {

    SYSTEM_ERROR("AUTH-10000","出错啦，后台小哥正在努力修复中"),
    PARAM_NOT_VALID("AUTH-10001","参数错误"),



    ;

    // 异常码
    private final String errorCode;
    // 错误信息
    private final String errorMessage;

}
