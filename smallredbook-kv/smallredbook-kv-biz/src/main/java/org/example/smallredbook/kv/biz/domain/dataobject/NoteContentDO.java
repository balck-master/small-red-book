package org.example.smallredbook.kv.biz.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

/**
 * @Author: tzy
 * @Description: 笔记内容 DO类
 * @Date: 2024/9/3 19:44
 */

@Table("note_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteContentDO {
    @PrimaryKey("id")
    private UUID id;

    private String content;
}
