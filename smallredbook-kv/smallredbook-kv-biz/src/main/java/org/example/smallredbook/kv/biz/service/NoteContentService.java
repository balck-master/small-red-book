package org.example.smallredbook.kv.biz.service;

import org.example.framework.common.response.Response;
import org.example.smallredbook.kv.dto.req.AddNoteContentReqDTO;

/**
 * @Author: tzy
 * @Description: 笔记内容存储业务
 * @Date: 2024/9/3 20:33
 */
public interface NoteContentService {

    /**
     * 添加笔记内容
     * @param addNoteContentReqDTO
     * @return
     */
    Response<?> addNodeContent(AddNoteContentReqDTO addNoteContentReqDTO);
}
