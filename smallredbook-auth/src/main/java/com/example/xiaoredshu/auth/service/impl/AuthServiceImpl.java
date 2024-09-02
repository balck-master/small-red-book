package com.example.xiaoredshu.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.example.xiaoredshu.auth.constant.RedisKeyConstants;
import com.example.xiaoredshu.auth.enums.LoginTypeEnum;
import com.example.xiaoredshu.auth.enums.ResponseCodeEnum;
import com.example.xiaoredshu.auth.model.vo.user.UpdatePasswordReqVO;
import com.example.xiaoredshu.auth.model.vo.user.UserLoginReqVO;
import com.example.xiaoredshu.auth.rpc.UserRpcService;
import com.example.xiaoredshu.auth.service.AuthService;
import com.google.common.base.Preconditions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.framework.biz.context.holder.LoginUserContextHolder;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.example.framework.common.utils.JsonUtils;
import org.example.smallredbook.user.api.dto.resp.FindUserByPhoneRspDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;



/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/16 21:25
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Resource
    private RedisTemplate redisTemplate;



    @Resource
    private TransactionTemplate transactionTemplate;


    @Resource
    private UserRpcService userRpcService;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private PasswordEncoder passwordEncoder;


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
                FindUserByPhoneRspDTO findUserByPhoneRspDTO1 = userRpcService.findUserByPhone(phone);

                log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(findUserByPhoneRspDTO1));

                //RPC：调用远程服务，调用用户
                Long userIdTmp = userRpcService.registerUser(phone);

                // 若调研用户服务，返回的用户 ID为空，则提示登录失败
                if (Objects.isNull(userIdTmp)) {
                    throw new BizException(ResponseCodeEnum.LOGIN_FAIL);
                }
                userId = userIdTmp;
                break;
            case PASSWORD:{
                //密码登录
                //todo
                String password = userLoginReqVO.getPassword();
                // 根据手机号查询
                FindUserByPhoneRspDTO findUserByPhoneRspDTO = userRpcService.findUserByPhone(phone);
                //判断手机号是否注册
                if(Objects.isNull(findUserByPhoneRspDTO)){
                    throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
                }

                // 匹配密码是否一致
                boolean matches = passwordEncoder.matches(password, findUserByPhoneRspDTO.getPassword());
                if(!matches){
                    throw new BizException(ResponseCodeEnum.PHONE_OR_PASSWORD_ERROR);
                }
                userId = findUserByPhoneRspDTO.getId();
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
     * 用户登出
     * @return
     */
    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();

        log.info("==> 用户退出登录, userId: {}", userId);

        threadPoolTaskExecutor.submit(() -> {
            Long userId2 = LoginUserContextHolder.getUserId();
            log.info("==> 异步线程中获取 userId: {}", userId2);
        });


        // 退出登录 (指定用户 ID)
        StpUtil.logout(userId);
        return Response.success();
    }

    @Override
    public Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO) {
        String newPassword = updatePasswordReqVO.getNewPassword();
        //加密后的密码
        String encodePassword = passwordEncoder.encode(newPassword);

        //RPC : 调用用户服务：更新密码
        userRpcService.updatePassword(encodePassword);

        return Response.success();
    }


}
