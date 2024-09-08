package org.example.smallredbook.note.biz.rpc;

import jakarta.annotation.Resource;
import org.example.smallredbook.distributed.id.generator.constant.DistributedIdGeneratorFeignApi;
import org.springframework.stereotype.Component;

/**
 * @Author: tzy
 * @Description: 用户服务  ，分布式id生成远程服务
 * @Date: 2024/9/7 14:45
 */
@Component
public class DistributedIdGeneratorRpcService {
    @Resource
    private DistributedIdGeneratorFeignApi distributedIdGeneratorFeignApi;

    /**
     * 生成雪花算法id
     * 对于笔记 ID，我们选择雪花算法 ID 来生成, 入参的 key 随便填即可。
     * @return
     */
    public String getSnowflakeId(){
        return distributedIdGeneratorFeignApi.getSnowflakeId("test");
    }
}
