package org.example.framework.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * biz：Biz 是 Business 英文的缩写，代表业务的意思
 *
 * 业务异常类
 */
@Getter
@Setter
public class BizException extends RuntimeException {
    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

    public BizException(BaseExceptionInterface baseExceptionInterface) {
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }
}