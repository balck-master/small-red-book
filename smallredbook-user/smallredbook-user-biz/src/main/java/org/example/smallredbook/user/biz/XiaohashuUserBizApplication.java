package org.example.smallredbook.user.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/8/14 9:24
 */
@SpringBootApplication
@MapperScan("org.example.smallredbook.user.biz.domain.mapper")
@EnableFeignClients(basePackages = "org.example.smallredbook")
public class XiaohashuUserBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohashuUserBizApplication.class,args);
    }
}
