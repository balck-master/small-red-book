package com.example.xiaoredshu.auth.constant;

/**
 * @Author: tzy
 * @Description: redis键名 常量类 ，统一管理 Redis Key
 * @Date: 2024/7/13 17:38
 */
public class RedisKeyConstants {

    /**
     * 验证码 KEY 前缀
     */
    private static final String VERIFICATION_CODE_KEY_PREFIX = "verification_code:";

    /**
     * 构建验证码 KEY ==>redis中的key
     * @param phone
     * @return
     */
    public static String buildVerificationCodeKey(String phone) {
        return VERIFICATION_CODE_KEY_PREFIX + phone;
    }
}
