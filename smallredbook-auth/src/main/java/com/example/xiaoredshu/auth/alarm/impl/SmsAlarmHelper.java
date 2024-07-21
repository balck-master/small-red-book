package com.example.xiaoredshu.auth.alarm.impl;

import com.example.xiaoredshu.auth.alarm.AlarmInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: tzy
 * @Description: 短信告警信息
 * @Date: 2024/7/21 16:48
 */
@Slf4j
public class SmsAlarmHelper implements AlarmInterface {

    /**
     * 发送告警信息
     * @param message
     * @return
     */
    @Override
    public boolean send(String message) {
        log.info("==> 【短信告警】：{}", message);

        // 业务逻辑...

        return true;
    }
}
