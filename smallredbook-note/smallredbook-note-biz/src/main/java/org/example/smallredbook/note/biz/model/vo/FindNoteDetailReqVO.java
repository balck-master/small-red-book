package org.example.smallredbook.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: tzy
 * @Description: 查询笔记详情
 * @Date: 2024/9/10 20:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindNoteDetailReqVO {
    @NotNull(message = "笔记 ID 不能为空")
    private Long id;
}
