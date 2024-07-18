package org.example.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逻辑删除
 */
@Getter
@AllArgsConstructor
public enum DeletedEnum {

    YES(true),
    NO(false);

    private final Boolean value;
}