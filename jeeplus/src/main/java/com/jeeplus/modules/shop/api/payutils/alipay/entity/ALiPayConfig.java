package com.jeeplus.modules.shop.api.payutils.alipay.entity;

/**
 * 支付宝支付所需的必要参数 理想情况下只需配置此处
 */
public class ALiPayConfig {
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088731242287303";

	// appid
	public static String appid = "2021001104631272";

	// 商户支付宝账号
	public static String seller_email = "187685@qq.com";

	// 商户真实姓名
	public static String account_name = "苏州一千八信息科技有限公司";

	// URL
	public static String url = "https://openapi.alipay.com/gateway.do";
	public static String timeoutExpress = "1000";

	// 商户的私钥RSA
	public static String private_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCyiABemXHaO/Ge7rcipMXk3Lr09oGiRDETysqMRusMK3lDL96NCUkupfhvLICNQxrUOBFFU4oPn3xs4jVNr914Ibmx+/i2JLNeyduWSV+3xEBdzumd2lz5royEaGwTLX4shwvyxtMCM5hf0yQvVMfZB2EPYg24j7casDQFG5yR05FgjIgiD4dNi5KaCnflJWRrObFhrSSNLkwpFE8qG4WXmtHUZ1CxPXCH1jNACXcTCJvkfuloxdCrL5L91R16GqN+H5DwHlx5RgdApIF92fE1Xr8MDA+ceDSLE5NhAvdNetjUmVJuRCXivxywUK4U62NTRBYPTh5GANsLoUypiaMVAgMBAAECggEBAIvzDMcbZiJ7BwddffYMUIz9cxj/+9LlvcsUFv8rwKmDksVWSxEoDFQjCsKrkgIqbmeZpABCYmGo1fLMyJgjmmrHlTeum+K8lldhyV7gGcaGcyaWQo/ZUzTu3XwWF1JZQSVKn9UqgtKg+3tymlT4EslQfYd/h9372r9zDPRMFspASTaY1aRGzMZ8KwkMPx97s7z08tS4RqnUwxZuBfkgLK1n41ziz1RHy59WSbAd0HwsRTJD5eGNGJyEd/IEAhF+mCyHXQIeRo2/euLoTX/VYKERmtabAy2SX7QDqca6PSyZ3tzGy3qRiBJh8ReXvN4FXhecc7nmFGz2NJg2B9/e6ikCgYEA/OCWAPBVB74K88baqUSuksMGN+ZRYkJaTfGsv/B5hkOFe/WxtSzvNA2Z/I9CpH5/SkkZRlg6R/r/c0qaiPb3q/DUTOLawliHGO+i5yLojUoSjhvh+WBIDT9Rcq103ukX76dUMWlxSv8dqMAkDENIQZI7UDzZWbczTFGV/Hsy7EcCgYEAtLxjLXvLamBJxOteP5qt/s9vLsR25llmxQZbDMrrFweuH1JLtNC9f6TATLQlzoi2enxEcGZJ7RQ4/cr9AkqqSe0miv5J3g6T0HS6mipLO5BlN3HqCR3krwC3bNc2u5A7bX5yJQxfeVTGrdLjxHZdwc4jE6A83byvEsag2Dgcj8MCgYAJSpc4YZw+NJVib6QJJCTl75SC6UuEOLvosUCPrN5KOKDm9RgaeBQx7W4DAaCJOryilhz/oK5Dhs1sBwm2jpx7DNZAHPW8vlP4bpWluHqi9l3IKu4ao5cCJZlwhT8OEbJ3c8Z8KkunXchRMv4gFTjKb/0N7a5uxXR1EWpZZI1ycQKBgQCsHuDgtw2A0BRQ6coHDrr3ePssccO+GZQKvMPHShRbCWBkyc35c/WT8wWDxuheacpGmcPfuzpaArFH2aHcJdlIsAXNxt/4Dq/sjM4M9YxSPHB4Pg8hAYwAsdD9YXPpajF+VDZGXCMsfHT9o2Jwfj1JzZOQJEDy9rdQTUNuxvlEWwKBgQDycl6ko4Wk8OYcf14STPnGbuqwbW6wEPrCuGBYGdB+umOlMDKAB0l2GjOYw0ewnes8emYrCplSiGZ5ayIaXEAdyne5DxjbjVboHSug+3AJQs6fTZbLe/QS7YYUprhv9BpqemLDkTKSYhTYPAC+CtyHJca30hZpqEEZ287qO9+H+g==";
	// 支付宝的公钥 RSA
	public static String ali_public_key ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1p11BKMtySf/cb99KFDDyqgvwWuDFmS30yNnczj3QRNyAcPNoX6XVyAXS0itk7Ob+ce6awh3Vao+2EuGFagxVeVn7svKBSLaTCKhRdztkUmBMuvQC8J/kigCktvLYDru2KyBJUnt1bwfMla4HWBg58ZCUIVgS9BIKZgph8rqVN7kN4/ayrYxRnxoir8C6gxD0PCnIvdCRCy+t5+4ZhHlnA4wZM79OEin+BU7CxER/mGq1xtk2bQg+QwNw7OzD5zLEUGDAFBECP0Y+tCzezbUCNBZp/o7h3uAsZVhqLW25QIT7kxxdnU5mMdQnx3wmZo5HmWoAiqSkPz84sUWmyVxOwIDAQAB";
	// 签名方式 (支付回调签名方式)
	public static String pay_sign_type = "RSA2";

	/**
	 * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
	 * 这里需要测试的话，需要用外网测试。https://www.ngrok.cc/ 这里有免费和付费的，实际上，免费用一下就可以了。
	 */
//	public static String notify_url_member = "http://lh.free.idcfengye.com/jeeplus/a/api/notify/alipay/notify_url_for_member";
	public static String notify_url_order = "http://115.159.107.111/jeeplus/a/api/notify/alipay/notify_url_pay_order";
	public static String notify_url_margin = "http://115.159.107.111/jeeplus/a/api/notify/alipay/notify_url_pay_margin";
	public static String notify_url_peisong = "http://129.211.73.47:8088/auto_food/pay/notify_url_for_ps_order";
	// 商品的标题/交易标题/订单标题/订单关键字等。
	public static String subject = "商品购买";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 接口名称 固定为：alipay.trade.app.pay
	public static String method = "alipay.trade.app.pay";

	// 调用的接口版本，固定为：1.0
	public static String version = "1.0";

	// 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
	public static String product_code = "QUICK_MSECURITY_PAY";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

}
