package org.example.smallredbook.note.biz.domain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.smallredbook.note.biz.domain.mapper")
public class XiaoredshuNoteBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoredshuNoteBizApplication.class, args);
    }

}