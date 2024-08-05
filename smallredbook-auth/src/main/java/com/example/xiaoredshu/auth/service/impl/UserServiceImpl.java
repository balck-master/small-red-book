package com.example.xiaoredshu.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.example.xiaoredshu.auth.constant.RedisKeyConstants;
import com.example.xiaoredshu.auth.constant.RoleConstants;
import com.example.xiaoredshu.auth.domain.dataobject.RoleDO;
import com.example.xiaoredshu.auth.domain.dataobject.UserDO;
import com.example.xiaoredshu.auth.domain.dataobject.UserRoleDO;
import com.example.xiaoredshu.auth.domain.mapper.RoleDOMapper;
import com.example.xiaoredshu.auth.domain.mapper.UserDOMapper;
import com.example.xiaoredshu.auth.domain.mapper.UserRoleDOMapper;
import com.example.xiaoredshu.auth.enums.LoginTypeEnum;
import com.example.xiaoredshu.auth.enums.ResponseCodeEnum;
import com.example.xiaoredshu.auth.filter.HeaderUserId2ContextFilter;
import com.example.xiaoredshu.auth.filter.LoginUserContextHolder;
import com.example.xiaoredshu.auth.model.vo.user.UserLoginReqVO;
import com.example.xiaoredshu.auth.service.UserService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.example.framework.common.enums.DeletedEnum;
import org.example.framework.common.enums.StatusEnum;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.example.framework.common.utils.JsonUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.xml.crypto.dsig.keyinfo.PGPData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/16 21:25
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserRoleDOMapper userRoleDOMapper;
    @Resource
    private UserDOMapper userDOMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private RoleDOMapper roleDOMapper;

    @Override
    public Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO) {
        Integer type = userLoginReqVO.getType();
        String phone = userLoginReqVO.getPhone();
        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);


        Long userId = null;

        // 判断登录类型
        switch (loginTypeEnum) {
            case VERIFICATION_CODE:
                //验证码登录
                String verificationCode = userLoginReqVO.getCode();
                //guava ，不满足抛出异常 ==>不是空，继续走；为空，抛出异常
                Preconditions.checkArgument(StringUtils.isNotBlank(verificationCode) , "验证码不能为空");


                String redisKey = RedisKeyConstants.buildVerificationCodeKey(phone);
                String redisCode = (String) redisTemplate.opsForValue().get(redisKey);


                if(!StringUtils.equals(verificationCode,redisCode)){
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }
                UserDO userDO = userDOMapper.selectByPhone(phone);

                log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO));

                // 判断是否注册
                if (Objects.isNull(userDO)) {
                    // 若此用户还没有注册，系统自动注册该用户
                    // todo
                    userId = this.registerUser(phone);
                } else {
                    // 已注册，则获取其用户 ID
                    userId = userDO.getId();
                }
                break;
            case PASSWORD:{
                //todo

                break;
            }

            default:
                break;
        }

        // SaToken 登录用户, 入参为用户 ID
        StpUtil.login(userId);

        // 获取 Token 令牌
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 返回 Token 令牌
        return Response.success(tokenInfo.tokenValue);
    }

    /**
     * 注册用户
     * @param phone
     * @return 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long     registerUser(String phone) {
        return transactionTemplate.execute(status -> {
            try {
                //生成全局唯一id
                Long xiaohongshuId = redisTemplate.opsForValue().increment(RedisKeyConstants.XIAOHASHU_ID_GENERATOR_KEY);

                UserDO userDO = UserDO.builder()
                        .phone(phone)
                        .xiaohashuId(String.valueOf(xiaohongshuId)) // 自动生成小红书号 ID
                        .nickname("小红薯" + xiaohongshuId) // 自动生成昵称, 如：小红薯10000
                        .status(StatusEnum.ENABLE.getValue()) // 状态为启用
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue()) // 逻辑删除
                        .build();

                //添加入库
                userDOMapper.insert(userDO);

                int i = 1 / 0;

                // 获取刚刚添加入库的用户 ID
                Long userId = userDO.getId();

                UserRoleDO userRoleDO = UserRoleDO.builder()
                        .userId(userId)
                        .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue())
                        .build();
                userRoleDOMapper.insert(userRoleDO);

                //一个用户可以有多个角色
//                List<Long> roles = Lists.newArrayList();
//                roles.add(RoleConstants.COMMON_USER_ROLE_ID);
//                String key = RedisKeyConstants.buildUserRoleKey(phone);
//                redisTemplate.opsForValue().set(key,JsonUtils.toJsonString(roles));



                RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

                // 将该用户的角色 ID 存入 Redis 中，指定初始容量为 1，这样可以减少在扩容时的性能开销
                List<String> roles = new ArrayList<>(1);
                roles.add(roleDO.getRoleKey());

                String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
                redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

                return userId;
            } catch (Exception e) {
                status.setRollbackOnly(); // 标记事务为回滚
                log.error("==> 系统注册用户异常: ", e);
                return null;
            }
        });


    }

    /**
     * 用户登出
     * @return
     */
    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();

        // 退出登录 (指定用户 ID)
        StpUtil.logout(userId);
        return Response.success();
    }


}
