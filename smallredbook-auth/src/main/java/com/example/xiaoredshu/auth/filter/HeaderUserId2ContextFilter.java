package com.example.xiaoredshu.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.framework.common.constant.GlobalConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @Author: tzy
 * @Description: 提取请求头中的用户 ID 保存到上下文中，以方便后续使用
 * @Date: 2024/8/4 22:15
 */
@Slf4j
@Component
public class HeaderUserId2ContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 从请求头中获取用户 ID
        String userId = request.getHeader(GlobalConstants.USER_ID);

        log.info("## HeaderUserId2ContextFilter, 用户 ID: {}", userId);

        //判断请求头中是否存在用户ID
        if(StringUtils.isBlank(userId)){
            //若为空
            //将请求和响应传给过滤链的下一个过滤器
            chain.doFilter(request,response);
            return;
        }

        //如果header中存在userId，则存到ThreadLocal中
        log.info("=====设置userId到ThreadLocal中 ， 用户ID ：{}",userId);
        LoginUserContextHolder.setUserId(userId);

        try {
            chain.doFilter(request,response);
        }finally {
            //一定要删除ThreadLocal ， 防止内存泄漏
            LoginUserContextHolder.remove();
            log.info("====删除ThreadLocal ，userID：{}",userId);
        }


    }
}
