package org.example.smallredbook.note.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("org.example.smallredbook.note.biz.domain.mapper")
@EnableFeignClients("org.example.smallredbook")
public class XiaoredshuNoteBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoredshuNoteBizApplication.class, args);
    }

}