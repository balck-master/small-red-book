package org.example.smallredbook.note.biz.service;

import org.example.framework.common.response.Response;
import org.example.smallredbook.kv.dto.req.FindNoteContentReqDTO;
import org.example.smallredbook.note.biz.model.vo.FindNoteDetailReqVO;
import org.example.smallredbook.note.biz.model.vo.FindNoteDetailRspVO;
import org.example.smallredbook.note.biz.model.vo.PublishNoteReqVO;

/**
 * @Author: tzy
 * @Description: 笔记业务
 * @Date: 2024/9/7 15:26
 */
public interface NoteService {
    /**
     * 笔记发布
     * @param publishNoteReqVO
     * @return
     */
    Response<?> publishNote(PublishNoteReqVO publishNoteReqVO);


    /**
     * 笔记详情
     * @param findNoteDetailReqVO
     * @return
     */
    Response<FindNoteDetailRspVO> findNoteDetail(FindNoteDetailReqVO findNoteDetailReqVO);
}
