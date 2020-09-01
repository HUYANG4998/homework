package com.wxtemplate.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JsonWebsocket implements Serializable {

    private String mine;

    private String avatar;

    private String id;

    private String content;

    private String username;

    private String time;

    private String status;
}
