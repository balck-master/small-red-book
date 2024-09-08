package org.example.smallredbook.note.biz.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.biz.operationlog.aspect.ApiOperationLog;
import org.example.framework.common.response.Response;
import org.example.smallredbook.note.biz.model.vo.PublishNoteReqVO;
import org.example.smallredbook.note.biz.service.NoteService;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/9/7 21:06
 */
@RestController
@RequestMapping("/note")
@Slf4j
public class NoteController {

    @Resource
    private NoteService noteService;

    @PostMapping(value = "/publish")
    @ApiOperationLog(description = "笔记发布")
    public Response<?> publishNote(@Valid @RequestBody PublishNoteReqVO publishNoteReqVO){
        return noteService.publishNote(publishNoteReqVO);
    }
}
