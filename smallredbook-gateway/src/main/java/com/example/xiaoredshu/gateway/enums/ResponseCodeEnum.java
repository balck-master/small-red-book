package com.example.xiaoredshu.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.framework.common.exception.BaseExceptionInterface;

/**
 * @Author: tzy
 * @Description: 回复枚举类
 * @Date: 2024/7/24 17:41
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("500", "系统繁忙，请稍后再试"),
    UNAUTHORIZED("401", "权限不足"),


    // ----------- 业务异常状态码 -----------
    ;

    // 异常码
    private final String errorCode;
    // 错误信息
    private final String errorMessage;

}
