package com.example.xiaoredshu.auth.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/13 21:55
 */

@SpringBootTest
@Slf4j
class ThreadPoolConfigTest {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 测试线程池
     */
    @Test
    void testSubmit() {
        threadPoolTaskExecutor.submit(() -> log.info("异步线程中说: 犬小哈专栏"));
    }
}