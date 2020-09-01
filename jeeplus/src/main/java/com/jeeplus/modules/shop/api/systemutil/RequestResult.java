package com.jeeplus.modules.shop.api.systemutil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhh
 * @date 2019/12/20 0020
 */
@Getter
@Setter
@AllArgsConstructor
public class RequestResult {
    /** 状态码 */
    private int code;

    /** 返回body */
    private  String body;
}
