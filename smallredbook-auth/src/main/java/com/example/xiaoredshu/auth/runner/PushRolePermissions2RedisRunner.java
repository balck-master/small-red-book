package com.example.xiaoredshu.auth.runner;

import cn.hutool.core.collection.CollUtil;
import com.example.xiaoredshu.auth.constant.RedisKeyConstants;
import com.example.xiaoredshu.auth.domain.dataobject.PermissionDO;
import com.example.xiaoredshu.auth.domain.dataobject.RoleDO;
import com.example.xiaoredshu.auth.domain.dataobject.RolePermissionDO;
import com.example.xiaoredshu.auth.domain.mapper.PermissionDOMapper;
import com.example.xiaoredshu.auth.domain.mapper.RoleDOMapper;
import com.example.xiaoredshu.auth.domain.mapper.RolePermissionDOMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.example.framework.common.utils.JsonUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: tzy
 * @Description: 推送角色权限数据到 Redis 中
 * @Date: 2024/7/20 14:15
 */
@Slf4j
@Component
public class PushRolePermissions2RedisRunner implements ApplicationRunner {
    @Resource
    private RoleDOMapper roleDOMapper;
    @Resource
    private RolePermissionDOMapper rolePermissionDOMapper;
    @Resource
    private PermissionDOMapper permissionDOMapper;
    @Resource
    private RedisTemplate redisTemplate;
    // 权限同步标记 Key
    private static final String PUSH_PERMISSION_FLAG = "push.permission.flag";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("==> 服务启动，开始同步角色权限数据到 Redis 中...");

        // todo

        try {
            // 是否能够同步数据: 原子操作，只有在键 PUSH_PERMISSION_FLAG 不存在时，才会设置该键的值为 "1"，并设置过期时间为 1 天
            //  如果键不存在，则设置键值并返回 1（表示加锁成功）；如果键已存在，则返回 0（表示加锁失败）。
            Boolean canPushed  = redisTemplate.opsForValue().setIfAbsent(PUSH_PERMISSION_FLAG, "1", 1, TimeUnit.DAYS);

            //如果无法同步权限数据
            if(!canPushed){
                log.warn("==> 角色权限数据已经同步至 Redis 中，不再同步...");
                return;
            }


            //查询出所有的角色
            List<RoleDO> roleDOS = roleDOMapper.selectEnabledList();

            if(CollUtil.isNotEmpty(roleDOS)){
                //拿到所有角色id
                List<Long> roleIds = roleDOS.stream().map(RoleDO::getId).toList();

                //根据角色ID，批量查询出所有角色对应的权限
                List<RolePermissionDO> rolePermissionDOS = rolePermissionDOMapper.selectByRoleIds(roleIds);
                //根据角色ID分组，每个角色->多个权限
                Map<Long, List<Long>> roleIdPermissionIdsMap  = rolePermissionDOS.stream().collect(
                        Collectors.groupingBy(RolePermissionDO::getRoleId,
                                Collectors.mapping(RolePermissionDO::getPermissionId, Collectors.toList())));
                
                //查询 APP 端所有被启用的权限
                List<PermissionDO> permissionDOS = permissionDOMapper.selectAppEnabledList();
                //权限ID - 权限DO
                Map<Long, PermissionDO> permissionIdDOMap = permissionDOS.stream().collect(
                        Collectors.toMap(PermissionDO::getId, permissionDO -> permissionDO));

                //组织 角色ID-权限 关系
                HashMap<Long,List<PermissionDO>> roleIdPermissionDOMap = Maps.newHashMap();
                //循环所有角色
                roleDOS.forEach(roleDO -> {
                    //当前角色ID
                    Long roleId = roleDO.getId();
                    //当前角色 ID 对应的权限ID集合
                    List<Long> permissionIds = roleIdPermissionIdsMap.get(roleId);
                    if(CollUtil.isNotEmpty(permissionIds)){
                        List<PermissionDO> perDOS = Lists.newArrayList();
                        permissionIds.forEach(permissionId->{
                            // 根据权限 ID 获取具体的权限 DO 对象
                            PermissionDO permissionDO = permissionIdDOMap.get(permissionId);
                            perDOS.add(permissionDO);
                        });
                        roleIdPermissionDOMap.put(roleId,perDOS);
                    }
                });

                //同步至redis中，方便后序网关查询鉴权使用
                roleIdPermissionDOMap.forEach((roleId,permissionDO)->{
                    String redisKey = RedisKeyConstants.buildRolePermissionKey(roleId);
                    redisTemplate.opsForValue().set(redisKey, JsonUtils.toJsonString(permissionDO));
                });

            }
            log.info("==> 服务启动，成功同步角色权限数据到 Redis 中...");
        } catch (Exception e) {
            log.error("==> 同步角色权限数据到 Redis 中失败: ", e);
        }

    }
}
