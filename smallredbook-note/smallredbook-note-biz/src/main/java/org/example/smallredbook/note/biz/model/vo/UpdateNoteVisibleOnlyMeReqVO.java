package org.example.smallredbook.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: tzy
 * @Description: 修改笔记仅对自己可见
 * @Date: 2024/9/12 19:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateNoteVisibleOnlyMeReqVO {
    @NotNull(message = "笔记 ID 不能为空")
    private Long id;
}
