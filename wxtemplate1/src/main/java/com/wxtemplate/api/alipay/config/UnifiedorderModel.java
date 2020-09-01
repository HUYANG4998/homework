package com.wxtemplate.api.alipay.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UnifiedorderModel {
    
    /**微信开放平台申请的应用id**/
    private String appid;
    /**商户号*/
    private String mch_id;
    /**随机字符串*/
    private String nonce_str;
    /**签名(需要根据app应用商户密钥对整个统一下单实体对象就行签名)*/
    private String sign;
    /**签名方式*/
    private String sign_type;
    /**商品描述：腾讯充值中心-QQ会员充值*/
    private String body;
    /**附加数据**/
    private String attach;
    /**支付订单号*/
    private String out_trade_no;
    /**总金额(分)*/
    private Integer total_fee;
    /**终端IP(8.8.8.8)*/
    private String spbill_create_ip;
    /**异步通知地址*/
    private String notify_url;
    /**交易类型**/
    private String trade_type;


}