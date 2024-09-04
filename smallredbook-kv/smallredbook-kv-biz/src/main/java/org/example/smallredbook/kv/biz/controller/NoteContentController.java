package org.example.smallredbook.kv.biz.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jnr.ffi.provider.converters.CharSequenceArrayParameterConverter;
import org.example.framework.common.response.Response;
import org.example.smallredbook.kv.biz.domain.repository.NoteContentRepository;
import org.example.smallredbook.kv.biz.service.NoteContentService;
import org.example.smallredbook.kv.dto.req.AddNoteContentReqDTO;
import org.example.smallredbook.kv.dto.req.FindNoteContentReqDTO;
import org.example.smallredbook.kv.dto.rsp.FindNoteContentRspDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: tzy
 * @Description: 笔记内容
 * @Date: 2024/9/3 20:47
 */
@RestController
@RequestMapping("/kv")
public class NoteContentController {

    @Resource
    private NoteContentService noteContentService;
    @PostMapping(value = "/note/content/add")
    public Response<?> addNoteContent(@RequestBody @Valid AddNoteContentReqDTO addNoteContentReqDTO){
        return noteContentService.addNodeContent(addNoteContentReqDTO);
    }


    @PostMapping(value = "/note/content/find")
    public Response<FindNoteContentRspDTO> findNoteContent(@Valid @RequestBody FindNoteContentReqDTO findNoteContentReqDTO){
        return noteContentService.findNoteContent(findNoteContentReqDTO);
    }
}
