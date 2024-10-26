package org.example.smallredbook.user.relation.biz.consumer;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.example.framework.common.utils.JsonUtils;
import org.example.smallredbook.user.relation.biz.constant.MQConstants;

import org.example.smallredbook.user.relation.biz.domain.dataobject.FansDO;
import org.example.smallredbook.user.relation.biz.domain.dataobject.FollowingDO;
import org.example.smallredbook.user.relation.biz.domain.mapper.FansDOMapper;
import org.example.smallredbook.user.relation.biz.domain.mapper.FollowingDOMapper;
import org.example.smallredbook.user.relation.biz.model.dto.FollowUserMqDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author: tzy
 * @Description: 关注 、取关的消费者
 * @Date: 2024/10/25 19:33
 */
@Component
@Slf4j
//不知道发送模式,默认就是 点对点模式=>只有一个服务实例可以消费
@RocketMQMessageListener(consumerGroup = "xiaohashu_group" ,
                        topic = MQConstants.TOPIC_FOLLOW_OR_UNFOLLOW)
public class FollowUnfollowConsumer implements RocketMQListener<Message> {

    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private FollowingDOMapper followingDOMapper;
    @Resource
    private FansDOMapper fansDOMapper;

    // 每秒创建 5000 个令牌
    @Resource
    private RateLimiter rateLimiter ;
    @Override
    public void onMessage(Message message) {
        //使用令牌桶进行流量削峰:通过获取令牌，如果没有令牌可用，将阻塞，直到获得
        rateLimiter.acquire();


        //消息体
        String bodyJsonStr = new String(message.getBody());
        String tags = message.getTags();

        log.info("==> FollowUnfollowConsumer 消费者 消费了消息{}，tags:{}",bodyJsonStr,tags);

        //根据MQ 标签，判断操作类型
        if(Objects.equals(tags,MQConstants.TAG_FOLLOW)){//关注
            handleFollowTagMessage(bodyJsonStr);
        }else if(Objects.equals(tags,MQConstants.TAG_UNFOLLOW)){//取关
            //todo
        }
    }

    /**
     * 处理关注 的tag消息
     * @param bodyJsonStr
     */
    private void handleFollowTagMessage(String bodyJsonStr) {
        //将消息体Json转成DTO对象
        FollowUserMqDTO followUserMqDTO = JsonUtils.parseObject(bodyJsonStr, FollowUserMqDTO.class);
        if(Objects.isNull(followUserMqDTO)) return;

        //幂等性：通过联合唯一索引判断
        Long userId = followUserMqDTO.getUserId();
        Long followUserId = followUserMqDTO.getFollowUserId();
        LocalDateTime createTime = followUserMqDTO.getCreateTime();

        //编程式事务 : 关注表插入一条记录，同时往粉丝表插入一条记录。为了保证原子性
        boolean isSuccess = Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            try {

                int count = followingDOMapper.insert(FollowingDO.builder()
                        .followingUserId(followUserId)
                        .userId(userId)
                        .createTime(createTime)
                        .build());

                if(count >1 ) {
                    fansDOMapper.insert(FansDO.builder()
                            .userId(followUserId)
                            .fansUserId(userId)
                            .createTime(createTime)
                            .build());
                    }
                return true;
            }catch (Exception e){
                status.setRollbackOnly();// 标记事务为回滚
                log.error("",e);
            }
            return false;
        }));

        log.error("## 数据库添加记录结果：{}",isSuccess);
        //todo: 跟新redis中被关注用户的 ZSet粉丝列表

    }
}
