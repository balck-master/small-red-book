package com.example.xiaoredshu.auth.service.impl;

import cn.hutool.core.util.RandomUtil;

import com.example.xiaoredshu.auth.constant.RedisKeyConstants;
import com.example.xiaoredshu.auth.enums.ResponseCodeEnum;
import com.example.xiaoredshu.auth.model.vo.verificationcode.SendVerificationCodeReqVO;
import com.example.xiaoredshu.auth.service.VerificationCodeService;
import com.example.xiaoredshu.auth.sms.AliyunSmsHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/13 18:06
 */

@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private AliyunSmsHelper aliyunSmsHelper;


    @Override
    public Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        String phone = sendVerificationCodeReqVO.getPhone();
        String redisKey = RedisKeyConstants.buildVerificationCodeKey(phone);
        Boolean hasKey = redisTemplate.hasKey(redisKey);
        //存在
        if(hasKey){
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }

        //不存在，发验证码
        String verificationCode = RandomUtil.randomNumbers(6);

        // 调用第三方短信发送服务
       threadPoolTaskExecutor.submit(()->{
           String signName = "阿里云短信测试";
           String templateCode = "SMS_154950909";
           String templateParam = String.format("{\"code\":\"%s\"}", verificationCode);
           aliyunSmsHelper.sendMessage(signName,templateCode,phone,templateParam);
       });



        log.info("==> 手机号,{} , 验证码：{}",phone,verificationCode);

        redisTemplate.opsForValue().set(redisKey,verificationCode,3, TimeUnit.MINUTES);

        return Response.success();
    }


}
