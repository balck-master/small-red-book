package com.example.xiaoredshu.auth.alarm.impl;

import com.example.xiaoredshu.auth.alarm.AlarmInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: tzy
 * @Description: 邮件警告通知类
 * @Date: 2024/7/21 16:32
 */

@Slf4j
public class MailAlarmHelper implements AlarmInterface {

    /**
     *
     * @param message
     * @return
     */
    @Override
    public boolean send(String message) {
        log.info("==> 【邮件告警】：{}", message);

        // 业务逻辑...

        return true;
    }
}
