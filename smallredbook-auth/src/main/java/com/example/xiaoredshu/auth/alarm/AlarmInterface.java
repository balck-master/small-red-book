package com.example.xiaoredshu.auth.alarm;

/**
 * @Author: tzy
 * @Description: 告警接口
 *
 * 当系统发送异常时，需要给开发同学发送告警信息，提示开发同学速度修复系统问题。
 * 告警的形式可以是发送短信，也可以是发送邮件。
 * @Date: 2024/7/21 16:30
 */
public interface AlarmInterface {

    /**
     * 发送告警信息
     *
     * @param message
     * @return
     */
    boolean send(String message);
}
