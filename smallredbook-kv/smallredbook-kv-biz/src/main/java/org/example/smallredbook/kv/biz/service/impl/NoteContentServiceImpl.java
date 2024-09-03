package org.example.smallredbook.kv.biz.service.impl;

import jakarta.annotation.Resource;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.common.response.Response;
import org.example.smallredbook.kv.biz.domain.dataobject.NoteContentDO;
import org.example.smallredbook.kv.biz.domain.repository.NoteContentRepository;
import org.example.smallredbook.kv.biz.service.NoteContentService;
import org.example.smallredbook.kv.dto.req.AddNoteContentReqDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author: tzy
 * @Description: 笔记内容存储业务
 * @Date: 2024/9/3 20:39
 */
@Service
@Slf4j
public class NoteContentServiceImpl implements NoteContentService {
    @Resource
    private NoteContentRepository noteContentRepository;


    @Override
    public Response<?> addNodeContent(AddNoteContentReqDTO addNoteContentReqDTO) {
        //笔记id
        Long noteId = addNoteContentReqDTO.getNoteId();
        //笔记内容
        String content = addNoteContentReqDTO.getContent();
        NoteContentDO noteContentDO = NoteContentDO.builder()
                .id(UUID.randomUUID())
                .content(content)
                .build();


         noteContentRepository.save(noteContentDO);
        return Response.success();
    }
}
