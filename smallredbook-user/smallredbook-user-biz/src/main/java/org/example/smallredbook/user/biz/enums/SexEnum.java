package org.example.smallredbook.user.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/8/14 13:49
 */

@Getter
@AllArgsConstructor
public enum SexEnum {

    WOMEN(0),
    MAN(1);

    private final Integer value;

    /**
     * 判断传入的性别是否有效
     * 来验证给定的值是否是枚举中定义的有效枚举常量。
     * @param value
     * @return
     */
    public static boolean isValid(Integer value){
        for (SexEnum loginTypeEnum : SexEnum.values()) {
            if(Objects.equals(loginTypeEnum,value)){
                return true;
            }
        }
        return false;
    }
}
