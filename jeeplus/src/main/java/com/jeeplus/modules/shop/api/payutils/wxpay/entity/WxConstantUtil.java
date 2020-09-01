package com.jeeplus.modules.shop.api.payutils.wxpay.entity;

public class WxConstantUtil {
	/**
	 * 微信开发平台应用ID
	 */
	/*public static final String APP_ID = "wx92edf94acd6478d8";*/
	public static final String APP_ID = "wxabbbe270d99454bc";
	/**
	 * 应用对应的AppSecret
	 */
	public static final String APP_SECRET = "378ce59d673c626d1fc3ac48dcfb67a0";
	/**
	 * APP_KEY 商户平台---api安全---密钥
	 */
	public static final String APP_KEY = "4fZVANsICQCXf69zXY6rMcCBivm5odfH";
	/**
	 * 微信支付商户号
	 */
	public static final String MCH_ID = "1574049151";
	/**
	 * 商品描述
	 */
	public static final String BODY = "商品购买";
	/**
	 * 商户号对应的密钥 也是 商户平台---api安全---密钥 同上面那个APP_KEY
	 */
	public static final String PARTNER_key = "4fZVANsICQCXf69zXY6rMcCBivm5odfH";

	/**
	 * 商户id 同微信支付商户号
	 */
	public static final String PARTNER_ID = "1574049151";
	/**
	 * 常量固定值
	 */
	public static final String GRANT_TYPE = "client_credential";
	/**
	 * 获取预支付id的接口url
	 */
	public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 服务器回调通知url
	 */
//	public static String NOTIFY_URL = "http://lh.free.idcfengye.com/jeeplus/a/api/notify/wxpay/notify_url_for_member";
	public static String ORDER = "http://115.159.107.111/jeeplus/a/api/notify/wxpay/notify_url_weixin_order";
	public static String MARGIN = "http://115.159.107.111/jeeplus/a/api/notify/wxpay/notify_url_weixin_margin";
}
