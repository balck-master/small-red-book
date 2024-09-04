package org.example.smallredbook.kv.biz.service.impl;

import jakarta.annotation.Resource;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.example.smallredbook.kv.biz.domain.dataobject.NoteContentDO;
import org.example.smallredbook.kv.biz.domain.repository.NoteContentRepository;
import org.example.smallredbook.kv.biz.enums.ResponseCodeEnum;
import org.example.smallredbook.kv.biz.service.NoteContentService;
import org.example.smallredbook.kv.dto.req.AddNoteContentReqDTO;
import org.example.smallredbook.kv.dto.req.FindNoteContentReqDTO;
import org.example.smallredbook.kv.dto.rsp.FindNoteContentRspDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    /**
     * 添加笔记内容
     * @param addNoteContentReqDTO
     * @return
     */
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

    /**
     * 查询笔记内容
     * @param findNoteContentReqDTO
     * @return
     */
    @Override
    public Response<FindNoteContentRspDTO> findNoteContent(FindNoteContentReqDTO findNoteContentReqDTO) {
        //笔记id
        String noteId = findNoteContentReqDTO.getNoteId();
        //根据笔记id查询笔记
        Optional<NoteContentDO> optional = noteContentRepository.findById(UUID.fromString(noteId));

        //如果笔记不存在
        if(!optional.isPresent()){
            throw new BizException(ResponseCodeEnum.NOTE_CONTENT_NOT_FOUND);
        }

        NoteContentDO noteContentDO = optional.get();

        FindNoteContentRspDTO findNoteContentRspDTO = FindNoteContentRspDTO.builder()
                .noteId(noteContentDO.getId())
                .content(noteContentDO.getContent())
                .build();

        return Response.success(findNoteContentRspDTO);
    }
}
