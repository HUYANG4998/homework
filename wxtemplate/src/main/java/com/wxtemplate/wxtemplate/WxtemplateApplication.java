package com.wxtemplate.wxtemplate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableAsync
@MapperScan("com.wxtemplate.wxtemplate.api.mapper")
public class WxtemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxtemplateApplication.class, args);
    }

}
