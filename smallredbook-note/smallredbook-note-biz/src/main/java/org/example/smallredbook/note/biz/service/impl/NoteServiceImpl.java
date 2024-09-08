package org.example.smallredbook.note.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Preconditions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.framework.biz.context.holder.LoginUserContextHolder;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.example.smallredbook.note.biz.domain.dataobject.NoteDO;
import org.example.smallredbook.note.biz.domain.mapper.NoteDOMapper;
import org.example.smallredbook.note.biz.domain.mapper.TopicDOMapper;
import org.example.smallredbook.note.biz.enums.NoteStatusEnum;
import org.example.smallredbook.note.biz.enums.NoteTypeEnum;
import org.example.smallredbook.note.biz.enums.NoteVisibleEnum;
import org.example.smallredbook.note.biz.enums.ResponseCodeEnum;
import org.example.smallredbook.note.biz.model.vo.PublishNoteReqVO;
import org.example.smallredbook.note.biz.rpc.DistributedIdGeneratorRpcService;
import org.example.smallredbook.note.biz.rpc.KeyValueRpcService;
import org.example.smallredbook.note.biz.service.NoteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/9/7 15:26
 */
@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    @Resource
    private DistributedIdGeneratorRpcService distributedIdGeneratorRpcService;
    @Resource
    private KeyValueRpcService keyValueRpcService;
    @Resource
    private TopicDOMapper topicDOMapper;
    @Resource
    private NoteDOMapper noteDOMapper;

    /**
     * 发布笔记
     * @param publishNoteReqVO
     * @return
     */
    public Response<?> publishNote(PublishNoteReqVO publishNoteReqVO) {
        //笔记类型
        Integer type = publishNoteReqVO.getType();
        NoteTypeEnum noteTypeEnum = NoteTypeEnum.valueOf(type);

        //若非图文、视频，抛出业务异常
        if(Objects.isNull(noteTypeEnum)){
            throw new BizException(ResponseCodeEnum.NOTE_TYPE_ERROR);
        }

        String imgUris = null;
        //笔记内容是否为空
        Boolean isContentEmpty = true;

        String videoUri = null;
        switch (noteTypeEnum){
            case IMAGE_TEXT ://图文笔记
                List<String> imgUriList = publishNoteReqVO.getImgUris();
                //校验图片是否为空，校验图片数量  如果图片集合不是空的，就抛出异常
                Preconditions.checkArgument(CollUtil.isNotEmpty(imgUriList) , "笔记图片不能为空");
                Preconditions.checkArgument(imgUriList.size() <=8 ,"笔记图片不能超过8张");
                //将图片链接拼接，以逗号分隔
                imgUris = StringUtils.join(imgUriList,",");
                break;
            case VIDEO : //视频笔记
                videoUri = publishNoteReqVO.getVideoUri();
                //校验视频是否为空
                Preconditions.checkArgument(StringUtils.isNotBlank(videoUri) , "视频不能为空");
                break;
            default :
                break;
        }

        //RPC：调用分布式 id 服务，获取分布式id
        String snowflakeId = distributedIdGeneratorRpcService.getSnowflakeId();

        //笔记内容
        String content = publishNoteReqVO.getContent();
        String contentUUID = null;
        //如果笔记内容不为空
        if(StringUtils.isNotBlank(content)){
            isContentEmpty = false;
            contentUUID = UUID.randomUUID().toString();

            //RPC：调用kv键值服务，存储短文本
            boolean isSavedSuccess = keyValueRpcService.saveNoteContent(contentUUID, content);
            //存储失败
            if(!isSavedSuccess){
                throw new BizException(ResponseCodeEnum.NOTE_PUBLISH_FAIL);
            }
        }

        //话题
        Long topicId = publishNoteReqVO.getTopicId();
        String topicName = null;
        if(!Objects.isNull(topicId)){
            //获取话题名称
            topicName = topicDOMapper.selectNameByPrimaryKey(topicId);
        }

        // 发布者用户 ID
        Long creatorId = LoginUserContextHolder.getUserId();

        // 构建笔记 DO 对象
        NoteDO noteDO = NoteDO.builder()
                .id(Long.valueOf(snowflakeId))
                .isContentEmpty(isContentEmpty)
                .creatorId(creatorId)
                .imgUris(imgUris)
                .title(publishNoteReqVO.getTitle())
                .topicId(publishNoteReqVO.getTopicId())
                .topicName(topicName)
                .type(type)
                .visible(NoteVisibleEnum.PUBLIC.getCode())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .status(NoteStatusEnum.NORMAL.getCode())
                .isTop(Boolean.FALSE)
                .videoUri(videoUri)
                .contentUuid(contentUUID)
                .build();

        try {
            //笔记入库存储
             noteDOMapper.insert(noteDO);
        }catch (Exception e){
            log.error("==>笔记存储失败",e);

            //RPC：笔记保存失败，删除笔记内容
            if(StringUtils.isNotBlank(contentUUID)){
                keyValueRpcService.deleteNoteContent(contentUUID);
            }
        }

        return Response.success();
    }
}
