package org.example.smallredbook.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * @Author: tzy
 * @Description: 笔记发布
 * @Date: 2024/9/7 14:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishNoteReqVO {

    @NotNull(message = "笔记类型不能为空")
    private Integer type;

    private List<String> imgUris;

    private String videoUri;

    private String title;

    private String content;

    private Long topicId;
}
