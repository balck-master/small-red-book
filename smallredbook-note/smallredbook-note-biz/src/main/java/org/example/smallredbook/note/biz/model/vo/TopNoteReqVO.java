package org.example.smallredbook.note.biz.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: tzy
 * @Description: 笔记置顶/取消置顶
 * @Date: 2024/9/12 20:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopNoteReqVO {
    @NotNull(message = "笔记id不能为空")
    private Long id;
    @NotNull(message = "置顶状态不能为空")
    private Boolean isTop;
}
