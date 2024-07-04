package org.example.exception;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/4 22:04
 */
public interface BaseExceptionInterface {

    // 获取异常码
    String getErrorCode();

    // 获取异常信息
    String getErrorMessage();
}
