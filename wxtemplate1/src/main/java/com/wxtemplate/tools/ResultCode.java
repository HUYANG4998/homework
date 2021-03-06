package com.wxtemplate.tools;

import lombok.Getter;

/**
 * 内置的返回值状态
 */
public enum ResultCode {
    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 失败
     */
    FAIL(0),
    /**
     * 未登录
     */
    UNAUTHORIZED(-2),
    /**
     * 网站关闭
     */
    WEBSITE_CLOSE(-3),
    /**
     * 内部错误(出现未捕获的异常)
     */
    INTERNAL_SERVER_ERROR(-4),
    /**
     * 无权访问
     */
    ACCESS_DENIED(-5);

    /**
     * 状态码
     */
    @Getter
    private final int code;

    ResultCode(int code) {
        this.code = code;
    }
}
