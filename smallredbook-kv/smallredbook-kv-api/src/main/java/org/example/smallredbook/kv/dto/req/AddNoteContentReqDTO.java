package org.example.smallredbook.kv.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: tzy
 * @Description: 添加笔记内容 入参
 * @Date: 2024/9/3 20:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddNoteContentReqDTO {

    @NotNull(message = "笔记 ID 不能为空")
    private Long noteId;

    @NotBlank(message = "笔记内容不能为空")
    private String content;
}
