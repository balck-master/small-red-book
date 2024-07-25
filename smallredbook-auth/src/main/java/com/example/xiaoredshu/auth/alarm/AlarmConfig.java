package com.example.xiaoredshu.auth.alarm;

import com.example.xiaoredshu.auth.alarm.impl.MailAlarmHelper;
import com.example.xiaoredshu.auth.alarm.impl.SmsAlarmHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: tzy
 * @Description: 告警配置类
 * @Date: 2024/7/21 16:58
 */

@Configuration
@RefreshScope //配置动态刷新功能:配置发生变化时，无需重启
public class AlarmConfig {

    @Value("${alarm.type}")
    private String alarmType;

    @Bean
    public AlarmInterface alarmInterface(){
        if(StringUtils.equals("sms",alarmType)){
            return new SmsAlarmHelper();
        }else if(StringUtils.equals("mail",alarmType)){
            return new MailAlarmHelper();
        }else {
            throw new IllegalArgumentException("错误的告警类型...");
        }
    }
}
