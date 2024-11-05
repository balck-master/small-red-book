package com.example.xiaoredshu.auth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableFeignClients("org.example.smallredbook")
public class XiaoredshuAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoredshuAuthApplication.class, args);
    }

}
