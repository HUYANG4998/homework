package com.jeeplus.modules.shop.api.payutils.wxpay;

import com.jeeplus.modules.shop.api.payutils.wxpay.entity.PrepayIdRequestHandler;
import com.jeeplus.modules.shop.api.payutils.wxpay.entity.ReturnXmlUtil;
import com.jeeplus.modules.shop.api.payutils.wxpay.entity.WxConstantUtil;
import com.jeeplus.modules.shop.api.payutils.wxpay.entity.WxNotifyParam;
import com.jeeplus.modules.shop.api.payutils.wxpay.util.Hutool;
import com.jeeplus.modules.shop.api.payutils.wxpay.util.Md5Util;
import com.jeeplus.modules.shop.api.payutils.wxpay.util.WxUtil;
import com.jeeplus.modules.shop.api.payutils.wxpay.util.XMLUtil;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.text.DecimalFormat;
import java.util.*;

@Controller
public class WXpayController {
	@Autowired
	private StoreService storeService;


	private static final Logger logger = LoggerFactory.getLogger(WXpayController.class);
	private static  final  String success = "<xml> \n"+
			" <return_code><![CDATA[SUCCESS]]></return_code>\n"+
			" <return_msg><![CDATA[OK]]></return_msg>\n"+
			"</xml>";
	private static  final  String fail = "<xml> \n"+
			" <return_code><![CDATA[FAIL]]></return_code>\n"+
			" <return_msg><![CDATA[FAIL]]></return_msg>\n"+
			"</xml>";
	/**
	 * 初始化微信支付
	 *
	 * // * @param request // * @param response // * @param orderId // * @return
	 */
	public WxNotifyParam initWx(HttpServletRequest request, HttpServletResponse response, Double money, String out_trade_no, String type) {
		// Map<String, Object> map = new HashMap<String, Object>();
				// 获取生成预支付订单的请求类

		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);
				// 分--* 100
				new DecimalFormat("#.00").format(money);
				String totalFee = String.valueOf((int) (money * 100));
				// 上线后，将此代码放开
				prepayReqHandler.setParameter("appid", WxConstantUtil.APP_ID.trim());
				prepayReqHandler.setParameter("body", WxConstantUtil.BODY.trim());
				prepayReqHandler.setParameter("mch_id", WxConstantUtil.MCH_ID.trim());
				String nonce_str = WxUtil.getNonceStr();
				prepayReqHandler.setParameter("nonce_str", nonce_str);
				if(type.equals("0")){
					//订单
					prepayReqHandler.setParameter("notify_url", WxConstantUtil.ORDER.trim());
				}else{
					//保证金
					Store store=storeService.get(out_trade_no);
					out_trade_no = out_trade_no.substring(0, out_trade_no.length() - 3);
					out_trade_no=out_trade_no+"_"+store.getNum();
					prepayReqHandler.setParameter("notify_url", WxConstantUtil.MARGIN.trim());
				}


				//out_trade_no   为订单号 一般传 订单编号（例如：2019092122）
				prepayReqHandler.setParameter("out_trade_no", out_trade_no.trim());
				/*prepayReqHandler.setParameter("attach", pay_id.trim());*/
				//客户端IP
				prepayReqHandler.setParameter("spbill_create_ip", "47.100.110.119");
				String timestamp = WxUtil.getTimeStamp();
				prepayReqHandler.setParameter("time_start", timestamp.trim());
				prepayReqHandler.setParameter("total_fee", totalFee);
				prepayReqHandler.setParameter("trade_type", "APP");
				/**
				 * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
				 */
				prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign());
				prepayReqHandler.setGateUrl(WxConstantUtil.GATEURL.trim());
				String prepayid;
				WxNotifyParam param = new WxNotifyParam();
				Map<Object, Object> map = new HashMap<>();
				try {
					prepayid = prepayReqHandler.sendPrepay();
					// 若获取prepayid成功，将相关信息返回客户端
					if (prepayid != null && !prepayid.equals("")) {
						String signs = "appid=" + WxConstantUtil.APP_ID + "&noncestr=" + nonce_str
								+ "&package=Sign=WXPay&partnerid=" + WxConstantUtil.PARTNER_ID + "&prepayid=" + prepayid
								+ "&timestamp=" + timestamp + "&key=" + WxConstantUtil.APP_KEY;
						/**
						 * 签名方式与上面类似
						 */
						param.setPrepayId(prepayid);
						param.setSign(Md5Util.MD5Encode(signs, "utf8").toUpperCase());
						param.setAppId(WxConstantUtil.APP_ID);
						// 等于请求prepayId时的time_start
						param.setTimeStamp(timestamp);
						// 与请求prepayId时值一致
						param.setNonceStr(nonce_str);
						// 固定常量
						param.setPackages("Sign=WXPay");
						param.setPartnerId(WxConstantUtil.PARTNER_ID);
						logger.info("-----------》创建微信订单成功: " + param);
						logger.info("-----------》获取微信订单Id成功: " + out_trade_no);
					} else {
						return null;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return null;
				}
				return param;
	}

	/**
	 * 接收通知
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	@RequestMapping(value = "/wx/notify_url_ys", method = RequestMethod.POST)
	public void getTenPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入微信回调方法");
		System.out.println("sdddddddddddddddddddddddddddddddddddddddddddd"+"进入回调方法");
		PrintWriter writer = response.getWriter();
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("微信回调支付通知结果：" + result);
		Map<String, String> map = null;
		try {
			/**
			 * 解析微信通知返回的信息
			 */
			map = XMLUtil.doXMLParse(result);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("- - - - - - - - -");
		logger.info("= = = = = = = = =:" + map.get("return_code"));
		logger.info("= = = = = = = = =:" + map.get("return_code"));
		System.out.println("ss"+map.get("return_code"));
		// 若充值成功，则告知微信服务器收到通知
		if (map.get("return_code").equals("SUCCESS")) {
			if (map.get("result_code").equals("SUCCESS")) {
				logger.info("微信充值会员体系成功！");
				String out_trade_no = map.get("out_trade_no");
				 //此处开始写 充值之后的逻辑
				logger.error("微信支付回调成功");

				writeMessageToResponse(response, success);
			} else {
				logger.error("微信支付回调失败");
				writeMessageToResponse(response,fail);
			}
		}else{
			writeMessageToResponse(response, fail);
		}

	}


	/**
	 * 微信APP退款
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static boolean wxRefund(Double money, String out_trade_no) throws Exception {
		System.out.println("进入商家退款 out——trade_no"+out_trade_no+"钱"+money);
		System.out.println("进入商家退款 out——trade_no"+out_trade_no+"钱"+money);
		System.out.println("进入商家退款 out——trade_no"+out_trade_no+"钱"+money);
		Integer randomNumber = new Random().nextInt(900) + 100;
		String out_request_no = Hutool.getTime() + randomNumber;
		DecimalFormat df = new DecimalFormat("#0.00");
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		// appid
		parameters.put("appid", WxConstantUtil.APP_ID);
		// 商户号
		parameters.put("mch_id", WxConstantUtil.MCH_ID);
		parameters.put("nonce_str", WxUtil.getNonceStr());
		// 我们自己设定的退款申请号，约束为UK
		parameters.put("out_refund_no", out_request_no);
		// 订单号
		parameters.put("out_trade_no", out_trade_no);

		String totalPrice = String.valueOf((int) (money * 100));
		// 单位为分
		parameters.put("refund_fee", totalPrice);
		// 单位为分
		parameters.put("total_fee", totalPrice);
		String sign = PrepayIdRequestHandler.createSign("utf-8", parameters);
		parameters.put("sign", sign);

		String reuqestXml = XMLUtil.getRequestXml(parameters);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		//linux 路径 /home/wwwroot/web/oulWV_certificate/apiclient_cert.p12
		// 放退款证书的路径
		FileInputStream instream = new FileInputStream(new File("C:/certys/apiclient_cert.p12"));

		try {
			// 商户号
			keyStore.load(instream, WxConstantUtil.MCH_ID.toCharArray());
		} finally {
			instream.close();
		}

		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WxConstantUtil.MCH_ID.toCharArray())
				.build();// 商户号
		@SuppressWarnings("deprecation")
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx进入退款接口");
			// 退款接口
			HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");

			StringEntity reqEntity = new StringEntity(reuqestXml);
			// 设置类型
			reqEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();

				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println(entity.getContent());
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"));
					String text = "";
					String str = "";
					while ((text = bufferedReader.readLine()) != null) {
						str += text + "\n";
					}
					System.out.println(str);
					Map<String, String> map = ReturnXmlUtil.readStringXmlOut(str);
					if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
						return true;
					} else {
						return false;
					}
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return false;
	}






	protected void writeMessageToResponse(HttpServletResponse response, String message) {
		PrintWriter writer = null;
		try {
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			writer = response.getWriter();
			writer.write(message);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null){
				writer.close();
			}

		}
	}
	public static void main(String args[]) {
		System.out.println("123");
		try {
			boolean success = wxRefund(0.01, "b023dcf1f54c46dfa22955e954c70_14");
			if (success) {
				System.out.println("退款成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*String notifyStr = XMLUtil.setXML("SUCCESS", "");
		System.out.println(notifyStr);*/
	}
}
