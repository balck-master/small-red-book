package com.example.xiaoredshu.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.example.xiaoredshu.gateway.constant.RedisKeyConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 自定义权限验证接口扩展 
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("## 获取用户权限列表, loginId: {}", loginId);

        // 返回此 loginId 拥有的权限列表

        // todo 从 redis 获取
        return Collections.emptyList();
    }

    @SneakyThrows
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.info("## 获取用户角色列表, loginId: {}", loginId);

        // 返回此 loginId 拥有的角色列表

        // 构建 用户-角色 Redis Key
        String userRoleKey = RedisKeyConstants.buildUserRoleKey(Long.valueOf(loginId.toString()));

        // 根据用户 ID ，从 Redis 中获取该用户的角色集合
        String useRolesValue = (String) redisTemplate.opsForValue().get(userRoleKey);

        if(StringUtils.isBlank(userRoleKey)){
            return null;
        }

        // 将 JSON 字符串转换为 List<String> 集合
        return objectMapper.readValue(useRolesValue, new TypeReference<>() {});

    }

}