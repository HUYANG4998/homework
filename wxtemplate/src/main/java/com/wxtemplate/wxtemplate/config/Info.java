package com.wxtemplate.wxtemplate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "info")
@Data
public class Info {
    private String imgpath;

    private String imgget;

}
