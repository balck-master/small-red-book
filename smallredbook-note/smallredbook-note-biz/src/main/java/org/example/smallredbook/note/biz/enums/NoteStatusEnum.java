package org.example.smallredbook.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: tzy
 * @Description: 笔记状态枚举类
 * @Date: 2024/9/6 21:05
 */
@Getter
@AllArgsConstructor
public enum NoteStatusEnum {

    BE_EXAMINE(0), // 待审核
    NORMAL(1), // 正常展示
    DELETED(2), // 被删除
    DOWNED(2), // 被下架
    ;

    private final Integer code;

}
