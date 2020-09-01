package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.wxtemplate.api.mapper")
public class WxtemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxtemplateApplication.class, args);
    }

}
