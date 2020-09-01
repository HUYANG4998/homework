package com.wxtemplate.wxtemplate.api.pay.config;

public class UnifiedorderResponse {

    /**
     * 预付单信息
     **/
    private String prepay_id;
    /**
     * 签名
     **/
    private String nonce_str;
    /**
     * 微信开放平台申请的应用id
     **/
    private String appid;
    /**
     * 签名
     **/
    private String sign;
    /**
     * 请求方式
     **/
    private String trade_type;
    /**
     * 商户号id
     **/
    private String mch_id;
    /**
     * 返回提示信息
     **/
    private String return_msg;
    /**
     * 结果码
     **/
    private String result_code;
    /**
     * 返回码
     **/
    private String return_code;
    /**
     * 时间戳
     **/
    private String timestamp;
}