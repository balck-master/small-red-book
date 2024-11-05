package org.example.smallredbook.note.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Preconditions;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.example.framework.biz.context.holder.LoginUserContextHolder;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.example.framework.common.utils.JsonUtils;
import org.example.smallredbook.kv.dto.req.FindNoteContentReqDTO;
import org.example.smallredbook.note.biz.config.RocketMQConfig;
import org.example.smallredbook.note.biz.constant.MQConstants;
import org.example.smallredbook.note.biz.constant.RedisKeyConstants;
import org.example.smallredbook.note.biz.domain.dataobject.NoteDO;
import org.example.smallredbook.note.biz.domain.mapper.NoteDOMapper;
import org.example.smallredbook.note.biz.domain.mapper.TopicDOMapper;
import org.example.smallredbook.note.biz.enums.NoteStatusEnum;
import org.example.smallredbook.note.biz.enums.NoteTypeEnum;
import org.example.smallredbook.note.biz.enums.NoteVisibleEnum;
import org.example.smallredbook.note.biz.enums.ResponseCodeEnum;
import org.example.smallredbook.note.biz.model.vo.*;
import org.example.smallredbook.note.biz.rpc.DistributedIdGeneratorRpcService;
import org.example.smallredbook.note.biz.rpc.KeyValueRpcService;
import org.example.smallredbook.note.biz.rpc.UserRpcService;
import org.example.smallredbook.note.biz.service.NoteService;
import org.example.smallredbook.user.api.dto.resp.FindUserByIdRspDTO;
import org.springframework.data.redis.connection.RedisPipelineException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private UserRpcService userRpcService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 笔记详情本地缓存
     */
    private static final Cache<Long, String> LOCAL_CACHE = Caffeine.newBuilder()
            .initialCapacity(10000) // 设置初始容量为 10000 个条目
            .maximumSize(10000) // 设置缓存的最大容量为 10000 个条目
            .expireAfterWrite(1, TimeUnit.HOURS) // 设置缓存条目在写入后 1 小时过期
            .build();

    /**
     * 发布笔记
     *
     * @param publishNoteReqVO
     * @return
     */
    public Response<?> publishNote(PublishNoteReqVO publishNoteReqVO) {
        //笔记类型
        Integer type = publishNoteReqVO.getType();
        NoteTypeEnum noteTypeEnum = NoteTypeEnum.valueOf(type);

        //若非图文、视频，抛出业务异常
        if (Objects.isNull(noteTypeEnum)) {
            throw new BizException(ResponseCodeEnum.NOTE_TYPE_ERROR);
        }

        String imgUris = null;
        //笔记内容是否为空
        Boolean isContentEmpty = true;

        String videoUri = null;
        switch (noteTypeEnum) {
            case IMAGE_TEXT://图文笔记
                List<String> imgUriList = publishNoteReqVO.getImgUris();
                //校验图片是否为空，校验图片数量  如果图片集合不是空的，就抛出异常
                Preconditions.checkArgument(CollUtil.isNotEmpty(imgUriList), "笔记图片不能为空");
                Preconditions.checkArgument(imgUriList.size() <= 8, "笔记图片不能超过8张");
                //将图片链接拼接，以逗号分隔
                imgUris = StringUtils.join(imgUriList, ",");
                break;
            case VIDEO: //视频笔记
                videoUri = publishNoteReqVO.getVideoUri();
                //校验视频是否为空
                Preconditions.checkArgument(StringUtils.isNotBlank(videoUri), "视频不能为空");
                break;
            default:
                break;
        }

        //RPC：调用分布式 id 服务，获取分布式id
        String snowflakeId = distributedIdGeneratorRpcService.getSnowflakeId();

        //笔记内容
        String content = publishNoteReqVO.getContent();
        String contentUUID = null;
        //如果笔记内容不为空
        if (StringUtils.isNotBlank(content)) {
            isContentEmpty = false;
            contentUUID = UUID.randomUUID().toString();

            //RPC：调用kv键值服务，存储短文本
            boolean isSavedSuccess = keyValueRpcService.saveNoteContent(contentUUID, content);
            //存储失败
            if (!isSavedSuccess) {
                throw new BizException(ResponseCodeEnum.NOTE_PUBLISH_FAIL);
            }
        }

        //话题
        Long topicId = publishNoteReqVO.getTopicId();
        String topicName = null;
        if (!Objects.isNull(topicId)) {
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
        } catch (Exception e) {
            log.error("==>笔记存储失败", e);

            //RPC：笔记保存失败，删除笔记内容
            if (StringUtils.isNotBlank(contentUUID)) {
                keyValueRpcService.deleteNoteContent(contentUUID);
            }
        }

        return Response.success();
    }


    /**
     * 查询笔记详情
     *
     * @param findNoteDetailReqVO
     * @return
     */
    @SneakyThrows
    public Response<FindNoteDetailRspVO> findNoteDetail(FindNoteDetailReqVO findNoteDetailReqVO) {
        //查询的笔记id
        Long noteId = findNoteDetailReqVO.getId();
        Long userId = LoginUserContextHolder.getUserId();//当前登录用户

        //先从本地缓存中获取
        String findNoteDetailRspVOStrLocalCache  = LOCAL_CACHE.getIfPresent(noteId);
        if(StringUtils.isNotBlank(findNoteDetailRspVOStrLocalCache)){
            FindNoteDetailRspVO findNoteDetailRspVO = JsonUtils.parseObject(findNoteDetailRspVOStrLocalCache, FindNoteDetailRspVO.class);
            log.info("==>命中了本地缓存：{}",findNoteDetailRspVOStrLocalCache);
            //可见性校验
            checkNoteVisibleFromVO(userId,findNoteDetailRspVO);
            return Response.success(findNoteDetailRspVO);
        }

        //从redis缓存中获取
        String noteDetailRedisKey = RedisKeyConstants.buildNoteDetailKey(noteId);
        String noteDetailJson = (String) redisTemplate.opsForValue().get(noteDetailRedisKey);

        //若缓存中有该笔记的数据，则直接返回
        if(StringUtils.isNotBlank(noteDetailJson)){
            FindNoteDetailRspVO findNoteDetailRspVO = JsonUtils.parseObject(noteDetailJson, FindNoteDetailRspVO.class);
            //异步线程中奖用户信息存入本地缓存
            threadPoolTaskExecutor.submit(()->{
                LOCAL_CACHE.put(noteId , Objects.isNull(findNoteDetailRspVO) ?"null" : JsonUtils.toJsonString(findNoteDetailRspVO));
            });

            //可见性校验
            checkNoteVisibleFromVO(userId,findNoteDetailRspVO );
            return Response.success(findNoteDetailRspVO);
        }

        //若redis缓存中获取不到，则走数据库查询
        NoteDO noteDO = noteDOMapper.selectByPrimaryKey(noteId);

        //笔记不存在,抛出业务异常
        if (Objects.isNull(noteDO)) {
            threadPoolTaskExecutor.execute(()->{
                // 防止缓存穿透，将空数据存入 Redis 缓存 (过期时间不宜设置过长)
                // 保底1分钟 + 随机秒数
                long expireSeconds = 60+ RandomUtil.randomInt(60);
                redisTemplate.opsForValue().set(noteDetailRedisKey,"null",expireSeconds,TimeUnit.SECONDS);
            });
            throw new BizException(ResponseCodeEnum.NOTE_NOT_FOUND);
        }

        //可见性校验
        Integer visible = noteDO.getVisible();
        checkNoteVisible(visible, userId, noteDO.getCreatorId());

        //RPC: 调用用户服务 查看笔记创建者信息
        Long creatorId = noteDO.getCreatorId();
        CompletableFuture<FindUserByIdRspDTO> userResultFuture = CompletableFuture
                .supplyAsync(() -> userRpcService.findById(creatorId), threadPoolTaskExecutor);
        //RPC: 调用kv服务 查看笔记内容
        CompletableFuture<String> contentResultFuture = CompletableFuture.completedFuture(null);
        if(Objects.equals(noteDO.getIsContentEmpty(),Boolean.FALSE)) {
            contentResultFuture = CompletableFuture
                    .supplyAsync(()->keyValueRpcService.findNoteContent(noteDO.getContentUuid()));
        }

        CompletableFuture<String> finalContentResultFuture = contentResultFuture;
        CompletableFuture<FindNoteDetailRspVO> resultFuture = CompletableFuture.allOf(userResultFuture, contentResultFuture)
                .thenApply(s -> {
                    //获取 Future 返回的结果
                    FindUserByIdRspDTO findUserByIdRspDTO = userResultFuture.join();
                    String content = finalContentResultFuture.join();//为什么要用finalxxx.join,因为使用contentResultFuture可能会阻塞线程

                    //若查询的笔记类型为图文模式，还需要将图片链接拆分开，转成集合；
                    Integer noteType = noteDO.getType();
                    String imgUrisStr = noteDO.getImgUris();
                    //图文链接集合
                    List<String> imgUris = null;
                    if (Objects.equals(noteType, NoteTypeEnum.IMAGE_TEXT) && StringUtils.isNotBlank(imgUrisStr)) {
                        imgUris = List.of(imgUrisStr.split(","));
                    }

                    // 构建返参 VO 实体类
                    return FindNoteDetailRspVO.builder()
                            .id(noteDO.getId())
                            .type(noteDO.getType())
                            .title(noteDO.getTitle())
                            .content(content)
                            .imgUris(imgUris)
                            .topicId(noteDO.getTopicId())
                            .topicName(noteDO.getTopicName())
                            .creatorId(userId)
                            .creatorName(findUserByIdRspDTO.getNickName())
                            .avatar(findUserByIdRspDTO.getAvatar())
                            .videoUri(noteDO.getVideoUri())
                            .updateTime(noteDO.getUpdateTime())
                            .visible(noteDO.getVisible())
                            .build();
                });

        FindNoteDetailRspVO findNoteDetailRspVO = resultFuture.get();

        //异步线程中 将笔记详情存入redis中
        threadPoolTaskExecutor.execute(()->{
            String noteDetailJson1 = JsonUtils.toJsonString(findNoteDetailRspVO);
            // 过期时间（保底1天 + 随机秒数，将缓存过期时间打散，防止同一时间大量缓存失效，导致数据库压力太大）
            long expireSeconds = 60*60*24 + RandomUtil.randomInt(60*60*24);
            redisTemplate.opsForValue().set(noteDetailRedisKey,noteDetailJson1,expireSeconds,TimeUnit.SECONDS);
        });

        return Response.success(findNoteDetailRspVO);
    }

    /**
     * 更新笔记
     * @param updateNoteReqVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Response<?> updateNote(UpdateNoteReqVO updateNoteReqVO) {
        //笔记id ， 笔记类型
        Long noteId = updateNoteReqVO.getId();
        Integer type = updateNoteReqVO.getType();

        NoteTypeEnum noteTypeEnum = NoteTypeEnum.valueOf(type);
        if(Objects.isNull(noteTypeEnum)){
            //如果不是 图文，视频，就跑出异常
            throw  new BizException(ResponseCodeEnum.NOTE_TYPE_ERROR);
        }

        String imgUris = null;
        String videoUri = null;
        switch (noteTypeEnum) {
            case IMAGE_TEXT: // 图文笔记
                List<String> imgUriList = updateNoteReqVO.getImgUris();
                // 校验图片是否为空
                Preconditions.checkArgument(CollUtil.isNotEmpty(imgUriList), "笔记图片不能为空");
                // 校验图片数量
                Preconditions.checkArgument(imgUriList.size() <= 8, "笔记图片不能多于 8 张");

                imgUris = StringUtils.join(imgUriList, ",");
                break;
            case VIDEO: // 视频笔记
                videoUri = updateNoteReqVO.getVideoUri();
                // 校验视频链接是否为空
                Preconditions.checkArgument(StringUtils.isNotBlank(videoUri), "笔记视频不能为空");
                break;
            default:
                break;
        }


        // 当前登录用户 ID
        Long currUserId = LoginUserContextHolder.getUserId();
        NoteDO selectNoteDO = noteDOMapper.selectByPrimaryKey(noteId);

        // 笔记不存在
        if (Objects.isNull(selectNoteDO)) {
            throw new BizException(ResponseCodeEnum.NOTE_NOT_FOUND);
        }

        // 判断权限：非笔记发布者不允许更新笔记
        if (!Objects.equals(currUserId, selectNoteDO.getCreatorId())) {
            throw new BizException(ResponseCodeEnum.NOTE_CANT_OPERATE);
        }

        //话题
        Long topicId = updateNoteReqVO.getTopicId();
        String topicName = null;
        if(Objects.nonNull(topicId)){
            topicName = topicDOMapper.selectNameByPrimaryKey(topicId);

            //判断提交的话题，是否存在
            if(StringUtils.isBlank(topicName)){
                throw new BizException(ResponseCodeEnum.TOPIC_NOT_FOUND);
            }
        }

        //更新笔记元数据表t_note表
        String content = updateNoteReqVO.getContent();
        NoteDO noteDO = NoteDO.builder()
                .id(noteId)
                .isContentEmpty(StringUtils.isBlank(content))
                .imgUris(imgUris)
                .title(updateNoteReqVO.getTitle())
                .topicId(updateNoteReqVO.getTopicId())
                .topicName(topicName)
                .type(type)
                .updateTime(LocalDateTime.now())
                .videoUri(videoUri)
                .build();
        noteDOMapper.updateByPrimaryKey(noteDO);

        //删除redis缓存
        String noteDetailRedisKey = RedisKeyConstants.buildNoteDetailKey(noteId);
        redisTemplate.delete(noteDetailRedisKey);

        //删除本地缓存
//        LOCAL_CACHE.invalidate(noteId);
        //同步发送广播模式MQ，将所有实例中的本地缓存都删除掉
        rocketMQTemplate.syncSend(MQConstants.TOPIC_DELETE_NOTE_LOCAL_CACHE,noteId);
        log.info("====》MQ：删除笔记本地缓存发送成功..." );


        //笔记内容更新
        NoteDO noteDO1 = noteDOMapper.selectByPrimaryKey(noteId);
        String contentUuid = noteDO1.getContentUuid();
        //笔记内容是否更新成功
        boolean isUpdateContentSuccess = false;
        if(StringUtils.isBlank(content)){
            //若笔记为空，就删除笔记
            isUpdateContentSuccess = keyValueRpcService.deleteNoteContent(contentUuid);
        }else {
            //调用kv存储服务
            isUpdateContentSuccess = keyValueRpcService.saveNoteContent(contentUuid,content);
        }

        //若更新失败，抛出异常，回滚事务
        if(!isUpdateContentSuccess){
            throw new BizException(ResponseCodeEnum.NOTE_UPDATE_FAIL);
        }
        return Response.success();
    }

    /**
     * 删除本地笔记缓存
     * @param noteId
     */
    @Override
    public void deleteNoteLocalCache(Long noteId) {
        LOCAL_CACHE.invalidate(noteId);
    }

    /**
     * 删除笔记
     * @param deleteNoteReqVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Response<?> deleteNote(DeleteNoteReqVO deleteNoteReqVO){
        //笔记id
        Long noteId = deleteNoteReqVO.getId();

        NoteDO selectNoteDO = noteDOMapper.selectByPrimaryKey(noteId);

        // 判断笔记是否存在
        if (Objects.isNull(selectNoteDO)) {
            throw new BizException(ResponseCodeEnum.NOTE_NOT_FOUND);
        }

        // 判断权限：非笔记发布者不允许删除笔记
        Long currUserId = LoginUserContextHolder.getUserId();
        if (!Objects.equals(currUserId, selectNoteDO.getCreatorId())) {
            throw new BizException(ResponseCodeEnum.NOTE_CANT_OPERATE);
        }

        NoteDO noteDO = NoteDO.builder()
                .id(noteId)
                .status(NoteStatusEnum.DELETED.getCode())
                .updateTime(LocalDateTime.now())
                .build();
        int count = noteDOMapper.updateByPrimaryKeySelective(noteDO);

        //如果影响的行数为0，则表示该笔记不存在
        if(count == 0){
            throw new BizException(ResponseCodeEnum.NOTE_NOT_FOUND);
        }

        //更新成功，
        //删除redis中的缓存
        String noteDetailKey = RedisKeyConstants.buildNoteDetailKey(noteId);
        redisTemplate.delete(noteDetailKey);

        //同步发送广播模式 MQ，将所有实例中的本地缓存都删除掉
        rocketMQTemplate.syncSend(MQConstants.TOPIC_DELETE_NOTE_LOCAL_CACHE,noteId);
        log.info("==> MQ:删除笔记本地缓存发送成功");

        return Response.success();
    }

    @Override
    public Response<?> visibleOnlyMe(UpdateNoteVisibleOnlyMeReqVO updateNoteVisibleOnlyMeReqVO) {
        //笔记id
        Long noteId = updateNoteVisibleOnlyMeReqVO.getId();

        NoteDO selectNoteDO = noteDOMapper.selectByPrimaryKey(noteId);

        // 判断笔记是否存在
        if (Objects.isNull(selectNoteDO)) {
            throw new BizException(ResponseCodeEnum.NOTE_NOT_FOUND);
        }

        // 判断权限：非笔记发布者不允许修改笔记权限
        Long currUserId = LoginUserContextHolder.getUserId();
        if (!Objects.equals(currUserId, selectNoteDO.getCreatorId())) {
            throw new BizException(ResponseCodeEnum.NOTE_CANT_OPERATE);
        }

        NoteDO noteDO = NoteDO.builder()
                .id(noteId)
                .visible(NoteVisibleEnum.PRIVATE.getCode())
                .updateTime(LocalDateTime.now())
                .build();
        int count = noteDOMapper.updateVisibleOnlyMe(noteDO);

        // 若影响的行数为 0，则表示该笔记无法修改为仅自己可见
        if(count == 0){
            throw new BizException(ResponseCodeEnum.NOTE_CANT_VISIBLE_ONLY_ME);
        }

        //
        String noteDetailKey = RedisKeyConstants.buildNoteDetailKey(noteId);
        redisTemplate.delete(noteDetailKey);

        // 同步发送广播模式 MQ，将所有实例中的本地缓存都删除掉
        rocketMQTemplate.syncSend(MQConstants.TOPIC_DELETE_NOTE_LOCAL_CACHE,noteId);
        log.info("====> MQ：删除笔记本地缓存发送成功...");

        return Response.success();
    }

    /**
     * 笔记置顶/取消置顶
     * @param topNoteReqVO
     * @return
     */
    @Override
    public Response<?> topNote(TopNoteReqVO topNoteReqVO) {

        //笔记id 、是否置顶
        Long noteId = topNoteReqVO.getId();
        Boolean isTop = topNoteReqVO.getIsTop();
        Long creatorId = LoginUserContextHolder.getUserId();
        NoteDO noteDO = NoteDO.builder()
                .id(noteId)
                .isTop(isTop)
                .creatorId(creatorId)// 只有笔记所有者，才能置顶/取消置顶笔记
                .updateTime(LocalDateTime.now())
                .build();
        int count = noteDOMapper.updateIsTop(noteDO);

        // 若影响的行数为 0，则表示该笔记无法
        if(count == 0){
            throw new BizException(ResponseCodeEnum.NOTE_CANT_OPERATE);
        }

        //删除redis缓存
        String noteDetailKey = RedisKeyConstants.buildNoteDetailKey(noteId);
        redisTemplate.delete(noteDetailKey);

        // 同步发送广播模式 MQ，将所有实例中的本地缓存都删除掉
        rocketMQTemplate.syncSend(MQConstants.TOPIC_DELETE_NOTE_LOCAL_CACHE,noteId);
        log.info("====> MQ：删除笔记本地缓存发送成功...");

        return Response.success();
    }

    /**
     * 校验笔记的可见性
     *
     * @param visible    是否可见
     * @param currUserId 当前用户id
     * @param creatorId  笔记创建者id
     */
    private void checkNoteVisible(Integer visible, Long currUserId, Long creatorId) {
        // 如果笔记为仅自己可见 并且 当前用户不是作者 ， 就抛出异常
        if (Objects.equals(visible, NoteVisibleEnum.PRIVATE.getCode())
                && !Objects.equals(currUserId, creatorId)) {
            throw new BizException(ResponseCodeEnum.NOTE_PRIVATE);
        }
    }

    /**
     * 校验笔记的可见性（针对 VO 实体类）
     * @param userId
     * @param findNoteDetailRspVO
     */
    private void checkNoteVisibleFromVO(Long userId, FindNoteDetailRspVO findNoteDetailRspVO) {
        if (Objects.nonNull(findNoteDetailRspVO)) {
            Integer visible = findNoteDetailRspVO.getVisible();
            checkNoteVisible(visible,userId,findNoteDetailRspVO.getCreatorId());
        }
    }
}

