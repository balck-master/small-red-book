package org.example.framework.biz.context.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.biz.context.holder.LoginUserContextHolder;
import org.example.framework.common.constant.GlobalConstants;

import java.util.Objects;

/**
 * @Author: tzy
 * @Description: Feign 请求拦截器 ==> 用来获取上下文中的用户id
 * @Date: 2024/8/16 15:29
 */

@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //获取当前上下文中的用户ID
        Long userId = LoginUserContextHolder.getUserId();

        //若不为空，则添加到请求头中
        if(Objects.nonNull(userId)){
            requestTemplate.header(GlobalConstants.USER_ID,String.valueOf(userId));
            log.info("############ feign 请求设置请求头 userId:{}",userId);
        }
    }
}
