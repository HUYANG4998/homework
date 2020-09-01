package com.wxtemplate.wxtemplate.api.pay.config;

/**
 * 支付宝支付所需的必要参数 理想情况下只需配置此处
 */
public class ALiPayConfig {

	public static String appid = "2016101800718262";

	// URL
	public static String url = "https://openapi.alipaydev.com/gateway.do";

	// 商户的私钥RSA
	public final static String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCATpAuadXIjS5iEBRlm6Pm4DE5j1Sd9ymZbVSWLDOz9JR5wyoMXP8JB837ty461gkbJGGGM1DSrRgydiTCzrDZNMvAUZtWta6Q6aIhmjODSVdpGXv/1aB70VeSmwljNgt51qKEuc1wtwWHwKWVH379GYPY1LPKPRzQJKI4hOB9r3Gno6pRpifE+KXzB4eJn1c3fj+FJdTGkHBNi6+d30ZFb68RV/MIqy6CLvIV5t3Z91qM3woXd1NCujTOMFmlG2cCDTnhzAHzR1ALc/lsgsn0EQ857Utvil40ewIeJ7O8EM1v1p7LkrcNVO43dRnflATjxsWRS0h9XW426XEMQyIFAgMBAAECggEAZemBHkyUMRrqp5tsCBTqx9x1XOna5SnH0gUENc/gNWTkdh6tjGplbG/VjcU/JB6T1wo7qFOkM+KnIJW0t0NjNvN3j0eCognnIVVhoV0Q44visSk708aEFTT/YQRiHXp0/Q6dwdyvMIM/EkfTfbIptLGKhzTTpaSK+UzPySLKCSmso64gm8HuDrSViytjvvuwyC30h2tFD6XkHoDT9AcimgTCRXMwOboy/16ZqlngJtucSc2Av3SszZFrAs8tbh0EfqnlCTRrCYkcc0AyQIPYS+j4cV3OVtbEHHNSAUJwPgYMHRgIWFwmDgpKtvtOPP2LsPqHDRIEeeX8eTuD9gne9QKBgQC4HAwaMbws3r/DLKO374N2jgKeK4+hZGNimYz1ewbi/LfcCXgdmgFnGBQoHdjfHAsuuoFbCmLJ+bh4w9HSvC6/akr3unHfX859JjMi/3eXLVyZoUEZ0u+YK74amhr/RcM4qW6wwQ3xfuBP7ky92vmIyE0nmLC7yOPpxMJlqdMeowKBgQCyaF8R5y3BFOZHcK51tNDTuUfK2Z1g4lODMG/srEPYYL66RKq3o+RmwXkBY4R1lWpI0gcTyf+R8cH2Ky4tnVReof2yhE/9APP2amHD5ZXWzubWGmUm0FcRxfS5lHulEmKZd0qY9HqtnYWvPcNJQ8FEyxxp0asmUfUgV2tDqDQPNwKBgGOS38THA2QtMP+pQ/62Lp7PHYFYQ/1g4xOvwnn+CPJ5BiOeh3VTFtEgR76Orf21GCM0wcNwcY+VXP0Lw467MnlaTqN/FhleorozD9Ms4gxEf2GjwibqW6Use0u97z4DCBu8H16/ewASw9TXKHBCX1VoJ7pzrZpSg1mqjg0X9VQLAoGBAId+hv2E0pX6dm25YXYZriOuapzfqwvCt+RBrC9WIWy7BP9YWUOBIFz+ARWR7C6VHJRTlnZsCkJgFowL/VU9UK9SXefvXwRqbd4uqzNpMORkXKIsh7fzsLzZ1+P9pe4XSXT+SVRjEKy8nqQZxy/kWmsRyvF/AHno8r7uRZOrsmX/AoGBALQ8YqJmUxyW+zmSgjGW1CbgO7q/MUUPSsbrpb3E4n4J3tjrTRTYDWvgPIG4EI/k+ekxEmaybtL/pM8C5/aeMsK9uiwS4oVV5aQmn/Lmh18IVIM+Y37ljDuyRKwR6UZ7MgkmtzDYVuJmosLWz1W7ZnmSanBEpT10pfsjrt66iCpD";
	// 支付宝的公钥 RSA
	public final static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtcGuMnkMfbxmHmIhYi9TBWHuhYmzpHMx5LmMvzpuE4f0oVzBlGqrbLdpHFweFv4V0CHq5VI3aXy7ljoi6gyIQISkjQBDD+W2Fn7eILpNuuzMgejxZVM56D3sa5o9XFo8FBhstRe2BE7KZBlshZN1RN3FOmth8NKNM0CoVL3LlCivDKnUJN1zExKnwS9qQ/S37T7G0gWYUvH0/Cqyzg61LnCY1iQBQ2UYrQ0hpb0EwP/nkFE3misyjV8/ygbvPq9Qy3fRCb0+70EjrJrQDFb6ddDqK021CymM4NW3z2zw2CNPcvLZdtj06YcPPjN7Ua1YmXUQIbWvO+h2CDIQWL2y7QIDAQAB"; // 签名方式
																																																																																																														// (支付回调签名方式)

	/**
	 * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
	 * 这里需要测试的话，需要用外网测试。https://www.ngrok.cc/ 这里有免费和付费的，实际上，免费用一下就可以了。
	 */
	public static String notify_url = "	http://47.100.110.119:9621/run/Alipay/notify_url";
	// 商品的标题/交易标题/订单标题/订单关键字等。
	public static String subject = "商品购买";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 接口名称 固定为：alipay.trade.app.pay
	public static String method = "alipay.trade.app.pay";

	// 调用的接口版本，固定为：1.0
	public static String version = "1.0";
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";


}
