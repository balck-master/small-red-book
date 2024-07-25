package com.example.xiaoredshu.auth.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/13 16:38
 */

@SpringBootTest
@Slf4j
class RedisTemplateConfigTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void redisTemplate() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name","tang");
    }

    /**
     * 判断某个 key 是否存在
     */
    @Test
    void testHasKey() {
        log.info("key 是否存在：{}", Boolean.TRUE.equals(redisTemplate.hasKey("dandan")));
    }

    /**
     * 获取某个 key 的 value
     */
    @Test
    void testGetValue() {
        log.info("value 值：{}", redisTemplate.opsForValue().get("name"));
    }

    /**
     * 删除某个 key
     */
    @Test
    void testDelete() {
        redisTemplate.delete("name");
    }

}