package com.example.xiaoredshu.gateway.filter;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.example.xiaoredshu.gateway.constant.RedisKeyConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.swing.plaf.basic.BasicButtonUI;
import java.util.List;
import java.util.Objects;

/**
 * @Author: tzy
 * @Description: 过滤器，转发请求时，将用户 ID 添加到 Header 请求头中，透传给下游服务
 * @Date: 2024/7/30 21:03
 */

@Component
@Slf4j
public class AddUserId2HeaderFilter implements GlobalFilter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 请求头中，用户ID的键
     */
    private static final String HEADER_USER_ID = "userId";

    /**
     * Header 头中 Token 的 Key
     */
    private static final String TOKEN_HEADER_KEY = "Authorization";

    /**
     * Token 前缀
     */
    private static final String TOKEN_HEADER_VALUE_PREFIX = "Bearer ";

    /**
     *  自定义过滤器 ， 假如请求头没有携带token，或者根据token获取的用户id为空，就放行
     *  否则，将用户 ID 设置到转发路由的请求头中；
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("==============>TokenConvertFilter");

      //从请求头中获取token数据
        List<String> tokenList = exchange.getRequest().getHeaders().get(TOKEN_HEADER_KEY);
        if(CollUtil.isEmpty(tokenList)){
            //若请求头中未携带token，则直接放行
            return chain.filter(exchange);
        }

        //获取token值
        String tokenValue = tokenList.get(0);
        //将token前缀去掉
        String token = tokenValue.replace(TOKEN_HEADER_VALUE_PREFIX, "");

        String tokenRedisKey = RedisKeyConstants.SA_TOKEN_TOKEN_KEY_PREFIX + token;
        Integer userId = (Integer)redisTemplate.opsForValue().get(tokenRedisKey);

        if(Objects.isNull(userId)){
            //若没有登录，直接放行
            return chain.filter(exchange);
        }



        log.info("## 当前登录的用户 ID：{}",userId);

        ServerWebExchange newExchange = exchange.mutate()
                .request(builder -> builder.header(HEADER_USER_ID, String.valueOf(userId))) // 将用户 ID 设置到请求头中
                .build();

        //将请求传递给过滤器链中的下一个过滤器进行处理，没有对请求进行任何修改。
        return chain.filter(newExchange);
    }
}
