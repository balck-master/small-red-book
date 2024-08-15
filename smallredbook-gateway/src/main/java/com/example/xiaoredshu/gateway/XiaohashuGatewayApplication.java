package com.example.xiaoredshu.gateway;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/22 14:59
 */

@SpringBootApplication(scanBasePackages = {"com.example.xiaoredshu.gateway.auth","com.example.xiaoredshu.gateway.config"})

public class XiaohashuGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaohashuGatewayApplication.class, args);
    }

}