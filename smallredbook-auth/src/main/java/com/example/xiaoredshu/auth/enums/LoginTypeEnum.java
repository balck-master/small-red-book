package com.example.xiaoredshu.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: tzy
 * @Description: 登录类型枚举类
 * @Date: 2024/7/16 20:57
 */


@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    // 验证码
    VERIFICATION_CODE(1),
    // 密码
    PASSWORD(2);

    //登录类型值
    private final int value;

    public static LoginTypeEnum valueOf(Integer type){
        //遍历这个Enum枚举类，将前端发过来的type 和 当前枚举值相比较，相同的话说明前端传过来的是验证码。
        for (LoginTypeEnum loginTypeEnum  : LoginTypeEnum.values()) {
            if(Objects.equals(loginTypeEnum.value,type)){
                return loginTypeEnum;
            }
        }

        //前端传来的不是1或者2
        return null;
    }

}
