package org.example.smallredbook.user.relation.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 执行Lua脚本返回结果
 */
@Getter
@AllArgsConstructor
public enum LuaResultEnum {
    // ZSET 不存在
    ZSET_NOT_EXIST(-1L),
    // 关注已达到上限
    FOLLOW_LIMIT(-2L),
    // 已经关注了该用户
    ALREADY_FOLLOWED(-3L),
    // 关注成功
    FOLLOW_SUCCESS(0L),
    // 未关注该用户
    NOT_FOLLOWED(-4L),

    ;

    private final Long code;

    /**
     * 根据类型 code 获取对应的枚举
     *
     * @param code
     * @return
     */
    public static LuaResultEnum valueOf(Long code) {
        for (LuaResultEnum luaResultEnum : LuaResultEnum.values()) {
            if (Objects.equals(code, luaResultEnum.getCode())) {
                return luaResultEnum;
            }
        }
        return null;
    }
}