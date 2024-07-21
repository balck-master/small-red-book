package com.example.xiaoredshu.auth.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.example.xiaoredshu.auth.alarm.AlarmConfig;
import com.example.xiaoredshu.auth.alarm.AlarmInterface;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {
    @Resource
    private AlarmInterface alarmInterface;

    @NacosValue(value = "${rate-limit.api.limit}", autoRefreshed = true)
    private Integer limit;

    @GetMapping("/test")
    public String test() {
        return "当前限流阈值为: " + limit;
    }

    @GetMapping("/alarm")
    public String sendAlarm(){
        alarmInterface.send("系统出错啦，犬小哈这个月绩效没了，速度上线解决问题！");
        return "success";
    }

}