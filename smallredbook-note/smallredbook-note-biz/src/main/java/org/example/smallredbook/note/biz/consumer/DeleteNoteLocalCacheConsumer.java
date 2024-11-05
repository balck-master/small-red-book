package org.example.smallredbook.note.biz.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.example.smallredbook.note.biz.constant.MQConstants;
import org.example.smallredbook.note.biz.service.NoteService;
import org.springframework.stereotype.Component;

/**
 * @Author: tzy
 * @Description: 删除笔记本地缓存消费者
 * @Date: 2024/9/12 13:52
 */
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = "xiaohashu_group", //消息的消费者组名
    topic = MQConstants.TOPIC_DELETE_NOTE_LOCAL_CACHE,//消费的主题 topic
        messageModel = MessageModel.BROADCASTING//广播模式
)
public class DeleteNoteLocalCacheConsumer implements RocketMQListener<String> {
    @Resource
    private NoteService noteService;

    @Override
    public void onMessage(String body) {
        Long noteId = Long.valueOf(body);
        log.info("## 消费者消费成功 ,noteId:{}",noteId);

        noteService.deleteNoteLocalCache(noteId);
    }
}
