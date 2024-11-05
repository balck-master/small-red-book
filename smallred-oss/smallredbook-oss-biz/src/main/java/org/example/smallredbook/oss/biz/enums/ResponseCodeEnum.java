package org.example.smallredbook.oss.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.framework.common.exception.BaseExceptionInterface;

@AllArgsConstructor
@Getter
public enum ResponseCodeEnum implements BaseExceptionInterface {

    SYSTEM_ERROR("OSS-10000","出错啦，后台小哥正在努力修复中"),
    PARAM_NOT_VALID("OSS-10001","参数错误"),


    // ----------- 业务异常状态码 -----------


    ;



    // 异常码
    private final String errorCode;
    // 错误信息
    private final String errorMessage;

}
