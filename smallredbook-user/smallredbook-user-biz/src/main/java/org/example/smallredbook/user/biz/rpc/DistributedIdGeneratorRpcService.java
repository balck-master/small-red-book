package org.example.smallredbook.user.biz.rpc;

import jakarta.annotation.Resource;
import org.example.smallredbook.distributed.id.generator.constant.DistributedIdGeneratorFeignApi;
import org.springframework.stereotype.Component;

/**
 * @Author: tzy
 * @Description: 分布式id生成 远程服务调用
 * @Date: 2024/9/5 14:23
 */
@Component
public class DistributedIdGeneratorRpcService {

    @Resource
    private DistributedIdGeneratorFeignApi distributedIdGeneratorFeignApi;

    /**
     * Leaf 号段模式：小哈书 ID 业务标识
     */
    private static final String BIZ_TAG_XIAOHASHU_ID = "leaf-segment-xiaohashu-id";
    /**
     * Leaf 号段模式：用户 ID 业务标识
     */
    private static final String BIZ_TAG_USER_ID = "leaf-segment-user-id";

    /**
     * 调用分布式 ID 生成服务生成小哈书 ID
     *
     * @return
     */
    public String getXiaohashuId() {
        return distributedIdGeneratorFeignApi.getSegmentId(BIZ_TAG_XIAOHASHU_ID);
    }
    /**
     * 调用分布式 ID 生成服务用户 ID
     *
     * @return
     */
    public String getUserId(){
        return distributedIdGeneratorFeignApi.getSegmentId(BIZ_TAG_USER_ID);
    }
}