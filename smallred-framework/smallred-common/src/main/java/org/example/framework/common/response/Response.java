package org.example.framework.common.response;

import lombok.Data;
import org.example.framework.common.exception.BaseExceptionInterface;
import org.example.framework.common.exception.BizException;

import java.io.Serializable;

/**
 * @Author: tzy
 * @Description:  响应参数工具类：
 * @Date: 2024/7/4 22:04
 */
@Data
public class Response<T> implements Serializable {

    //是否成功，默认为true
    private boolean success = true;
    //响应消息
    private String message;
    //异常码
    private String errorCode;
    //响应数据
    private T data;

    // =================================== 成功响应 ===================================
    public static <T> Response<T>  success(){
        Response<T> response = new Response<>();
        return response;
    }

    public static <T> Response<T>  success(T data){
        Response<T> response = new Response<>();
        response.setData(data);
        return response;
    }

    // =================================== 成功失败 ===================================
    public static <T> Response<T> fail() {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        return response;
    }

    public static <T> Response<T> fail(String message) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
    public static <T> Response<T> fail(String errorCode,String message) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrorCode(errorCode);
        return response;
    }

    public static <T> Response<T> fail(BizException bizException) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setErrorCode(bizException.getErrorCode());
        response.setMessage(bizException.getErrorMessage());
        return response;
    }

    public static <T> Response<T> fail(BaseExceptionInterface baseExceptionInterface) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setErrorCode(baseExceptionInterface.getErrorCode());
        response.setMessage(baseExceptionInterface.getErrorMessage());
        return response;
    }

}
