package com.example.xiaoredshu.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.example.xiaoredshu.auth.domain.mapper")
public class XiaoredshuAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoredshuAuthApplication.class, args);
    }

}
