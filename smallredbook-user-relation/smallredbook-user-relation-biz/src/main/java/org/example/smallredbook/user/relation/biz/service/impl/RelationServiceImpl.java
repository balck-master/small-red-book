package org.example.smallredbook.user.relation.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.example.framework.biz.context.holder.LoginUserContextHolder;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.example.framework.common.utils.DateUtils;
import org.example.framework.common.utils.JsonUtils;
import org.example.smallredbook.user.api.dto.resp.FindUserByIdRspDTO;
import org.example.smallredbook.user.relation.biz.constant.MQConstants;
import org.example.smallredbook.user.relation.biz.constant.RedisKeyConstants;
import org.example.smallredbook.user.relation.biz.domain.dataobject.FollowingDO;
import org.example.smallredbook.user.relation.biz.domain.mapper.FollowingDOMapper;
import org.example.smallredbook.user.relation.biz.enums.LuaResultEnum;
import org.example.smallredbook.user.relation.biz.enums.ResponseCodeEnum;
import org.example.smallredbook.user.relation.biz.model.dto.FollowUserMqDTO;
import org.example.smallredbook.user.relation.biz.model.dto.UnfollowUserMqDTO;
import org.example.smallredbook.user.relation.biz.model.vo.FollowUserReqVO;
import org.example.smallredbook.user.relation.biz.model.vo.UnfollowUserReqVO;
import org.example.smallredbook.user.relation.biz.rpc.UserRpcService;
import org.example.smallredbook.user.relation.biz.service.RelationService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/10/25 15:08
 */
@Service
@Slf4j
public class RelationServiceImpl implements RelationService {
    @Resource
    private UserRpcService userRpcService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private FollowingDOMapper followingDOMapper;

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    /**
     * 关注用户
     * @param followUserReqVO
     * @return
     */
    public Response<?> follow(FollowUserReqVO followUserReqVO) {
        // 关注的用户 ID
        Long followUserId = followUserReqVO.getFollowUserId();

        // 当前登录的用户 ID
        Long userId = LoginUserContextHolder.getUserId();

        // 校验：无法关注自己
        if (Objects.equals(userId, followUserId)) {
            throw new BizException(ResponseCodeEnum.CANT_FOLLOW_YOUR_SELF);
        }

        // 1.校验关注的用户是否存在
        FindUserByIdRspDTO findUserByIdRspDTO = userRpcService.findById(followUserId);

        if (Objects.isNull(findUserByIdRspDTO)) {
            throw new BizException(ResponseCodeEnum.FOLLOW_USER_NOT_EXISTED);
        }

        // 构建当前用户关注列表的 Redis Key
        String followingRedisKey = RedisKeyConstants.buildUserFollowingKey(userId);

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        // Lua 脚本路径
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/follow_check_and_add.lua")));
        // 返回值类型
        script.setResultType(Long.class);

        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 当前时间转时间戳
        long timestamp = DateUtils.localDateTime2Timestamp(now);

        // 执行 Lua 脚本，拿到返回结果
        Long result = (Long) redisTemplate.execute(script, Collections.singletonList(followingRedisKey), followUserId, timestamp);

        // 校验 Lua 脚本执行结果
        checkLuaScriptResult(result);

        // ZSET 不存在
        if (Objects.equals(result, LuaResultEnum.ZSET_NOT_EXIST.getCode())) {
            // 从数据库查询当前用户的关注关系记录
            List<FollowingDO> followingDOS = followingDOMapper.selectByUserId(userId);

            // 随机过期时间
            // 保底1天+随机秒数
            long expireSeconds = 60*60*24 + RandomUtil.randomInt(60*60*24);

            // 若记录为空，直接 ZADD 对象, 并设置过期时间
            if (CollUtil.isEmpty(followingDOS)) {
                DefaultRedisScript<Long> script2 = new DefaultRedisScript<>();
                script2.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/follow_add_and_expire.lua")));
                script2.setResultType(Long.class);

                // TODO: 可以根据用户类型，设置不同的过期时间，若当前用户为大V, 则可以过期时间设置的长些或者不设置过期时间；如不是，则设置的短些
                // 如何判断呢？可以从计数服务获取用户的粉丝数，目前计数服务还没创建，则暂时采用统一的过期策略
                redisTemplate.execute(script2, Collections.singletonList(followingRedisKey), followUserId, timestamp, expireSeconds);
            } else { // 若记录不为空，则将关注关系数据全量同步到 Redis 中，并设置过期时间；
                // 构建 Lua 参数
                Object[] luaArgs = buildLuaArgs(followingDOS, expireSeconds);

                // 执行 Lua 脚本，批量同步关注关系数据到 Redis 中
                DefaultRedisScript<Long> script3 = new DefaultRedisScript<>();
                script3.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/follow_batch_add_and_expire.lua")));
                script3.setResultType(Long.class);
                redisTemplate.execute(script3, Collections.singletonList(followingRedisKey), luaArgs);

                // 再次调用上面的 Lua 脚本：follow_check_and_add.lua , 将最新的关注关系添加进去
                result = (Long) redisTemplate.execute(script, Collections.singletonList(followingRedisKey), followUserId, timestamp);
                checkLuaScriptResult(result);
            }
        }

        // TODO: 发送 MQ
        // 构建消息对象
        FollowUserMqDTO followUserMqDTO = FollowUserMqDTO.builder()
                .userId(userId)
                .followUserId(followUserId)
                .createTime(LocalDateTime.now())
                .build();
        //将 DTO 转成JSON 字符串设置到消息体中
        Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(followUserMqDTO)).build();

        // 为MQ消息打上标签Tag： 格式--> Topic : Tag
        String destination =  MQConstants.TOPIC_FOLLOW_OR_UNFOLLOW + ":" + MQConstants.TAG_FOLLOW;
        log.info("==> 开始发送关注操作 MQ,消息体: {}",followUserMqDTO);

        // 【生产者】:异步发送MQ消息，表示消息要发送到的主题（Topic）或者队列（Queue）/ 消息 /回调接口，用于处理发送结果
        rocketMQTemplate.asyncSend(destination, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> MQ 发送成功，SendResult：{}",sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> MQ 发送异常 :",throwable );
            }
        });

        return Response.success();
    }

    @Override
    public Response<?> unfollow(UnfollowUserReqVO unfollowUserReqVO) {
        // 想要取关了用户 ID
        Long unfollowUserId = unfollowUserReqVO.getUnfollowUserId();
        // 当前登录用户 ID
        Long userId = LoginUserContextHolder.getUserId();

        // 无法取关自己
        if (Objects.equals(userId, unfollowUserId)) {
            throw new BizException(ResponseCodeEnum.CANT_UNFOLLOW_YOUR_SELF);
        }

        //2. 校验取关的用户是否存在
        FindUserByIdRspDTO findUserByIdRspDTO = userRpcService.findById(unfollowUserId);
        if(Objects.isNull(findUserByIdRspDTO)){
            throw new BizException(ResponseCodeEnum.FOLLOW_USER_NOT_EXISTED);
        }

        //3.必须是关注了的用户，才能取关
        String followingRedisKey = RedisKeyConstants.buildUserFollowingKey(userId);
        Double score = redisTemplate.opsForZSet().score(followingRedisKey, unfollowUserId);//following:userId:unfollowUserId
        if(Objects.isNull(score)){
            throw new BizException(ResponseCodeEnum.NOT_FOLLOWED);
        }
        //从自己的关注列表中移除
        redisTemplate.opsForZSet().remove(followingRedisKey,unfollowUserId);

        // 发送MQ
        UnfollowUserMqDTO unfollowUserMqDTO = UnfollowUserMqDTO.builder()
                .unfollowUserId(unfollowUserId)
                .userId(userId)
                .createTime(LocalDateTime.now())
                .build();
        //将DTO对象转为json
        MessageBuilder<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(unfollowUserMqDTO));
        //拼接消费者mq 发送的的主题 topic+tag
        String destination =  MQConstants.TOPIC_FOLLOW_OR_UNFOLLOW+":"+MQConstants.TAG_UNFOLLOW;
        log.info("==> 开始发送取关操作 MQ, 消息体: {}", unfollowUserMqDTO);

        rocketMQTemplate.asyncSend(destination, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> MQ 发送异常: ", throwable);
            }
        });
        return Response.success();
    }


    /**
     * 校验 Lua 脚本结果，根据状态码抛出对应的业务异常
     * @param result
     */
    private static void checkLuaScriptResult(Long result) {
        LuaResultEnum luaResultEnum = LuaResultEnum.valueOf(result);

        if (Objects.isNull(luaResultEnum)) throw new RuntimeException("Lua 返回结果错误");
        // 校验 Lua 脚本执行结果
        switch (luaResultEnum) {
            // 关注数已达到上限
            case FOLLOW_LIMIT -> throw new BizException(ResponseCodeEnum.FOLLOWING_COUNT_LIMIT);
            // 已经关注了该用户
            case ALREADY_FOLLOWED -> throw new BizException(ResponseCodeEnum.ALREADY_FOLLOWED);
        }
    }

    /**
     * 构建 Lua 脚本参数
     *
     * @param followingDOS
     * @param expireSeconds
     * @return
     */
    private static Object[] buildLuaArgs(List<FollowingDO> followingDOS, long expireSeconds) {
        int argsLength = followingDOS.size() * 2 + 1; // 每个关注关系有 2 个参数（score 和 value），再加一个过期时间
        Object[] luaArgs = new Object[argsLength];

        int i = 0;
        for (FollowingDO following : followingDOS) {
            luaArgs[i] = DateUtils.localDateTime2Timestamp(following.getCreateTime()); // 关注时间作为 score
            luaArgs[i + 1] = following.getFollowingUserId();          // 关注的用户 ID 作为 ZSet value
            i += 2;
        }

        luaArgs[argsLength - 1] = expireSeconds; // 最后一个参数是 ZSet 的过期时间
        return luaArgs;
    }
}
