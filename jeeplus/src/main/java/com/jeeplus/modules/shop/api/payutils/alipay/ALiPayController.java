
package com.jeeplus.modules.shop.api.payutils.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

import com.jeeplus.common.json.AjaxJson;

import com.jeeplus.modules.shop.api.payutils.alipay.entity.ALiPayConfig;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@CrossOrigin
@RequestMapping("/pay")
public class ALiPayController {


	@Autowired
	private StoreService storeService;
	@PostMapping("/pay_ali")
	@ResponseBody
	public AjaxJson finallyAliParam(String food_order_id, Double price, String type) {
		AjaxJson j=new AjaxJson();
		//根据订单id 获取 订单信息
		//如果存在 一个订单多次 付款的情况 可以先更新 一个订单号在进行二次付款
		//传输 订单号
//		String numbers = "20190333333";
//            String aliParam = this.payIn("",numbers,price,type);
//            j.put("200",aliParam);
            return j;
	}


	public String payIn(String orderNo, Double price,String type) {
		String param = "";
		try {
			// 实例化客户端
			AlipayClient alipayClient = new DefaultAlipayClient(
					"https://openapi.alipay.com/gateway.do",
					ALiPayConfig.appid, ALiPayConfig.private_key, "json",
					ALiPayConfig.input_charset, ALiPayConfig.ali_public_key,
					ALiPayConfig.pay_sign_type);
			// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
			AlipayTradeAppPayRequest request1 = new AlipayTradeAppPayRequest();
			// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setBody("欢迎使用么么");
			model.setSubject("么么花费:" +price + "元");

			model.setTimeoutExpress("30m");
			model.setTotalAmount(price + "");
			model.setSellerId(ALiPayConfig.partner);
			model.setProductCode("QUICK_MSECURITY_PAY");
			/*model.setOutTradeNo("");*/

			if(type.equals("0")){

                request1.setNotifyUrl(ALiPayConfig.notify_url_order);
            }else {
				//课程
				Store store=storeService.get(orderNo);
				orderNo = orderNo.substring(0, orderNo.length() - 3);
				orderNo=orderNo+"_"+store.getNum();
                request1.setNotifyUrl(ALiPayConfig.notify_url_margin);
            }
			model.setOutTradeNo(orderNo);
			request1.setBizModel(model);
			try {
				// 这里和普通的接口调用不同，使用的是sdkExecute
				AlipayTradeAppPayResponse response = alipayClient
						.sdkExecute(request1);
				System.out.println(response.getBody());
				param = response.getBody();
			} catch (AlipayApiException e) {
				e.getMessage();
				param = "error";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			param = "error";
		}
		return param;
	}


	//支付后异步请求地址
	@RequestMapping("/notify_url_for_order")
	public void doOrderSecPayNotify(HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入回调方法");
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			@SuppressWarnings("rawtypes")
			Map requestParams = request.getParameterMap();
			for (@SuppressWarnings("rawtypes")
			Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
					valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
					System.out.println(valueStr);
				}
				// 乱码解决，这段代码在出现乱码时使用。
				params.put(name, valueStr.trim());
			}
			/**
			 * System.out.println("--->获取支付宝POST过来反馈信息");
			 * 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。 boolean
			 * AlipaySignature.rsaCheckV1(Map<String, String> params, String
			 * publicKey, String charset, String sign_type)
			 */
			try {
				boolean flag = AlipaySignature.rsaCheckV1(params,
						ALiPayConfig.ali_public_key,
						ALiPayConfig.input_charset, "RSA2");
				// boolean flag = AlipaySignature.rsaCheckV1(params,
				// ALiPayConfig.ali_public_key,
				// "UTF-8",ALiPayConfig.pay_sign_type);
				// 商户订单号
				String out_trade_no = new String(request.getParameter(
						"out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				// 支付宝交易号
				String trade_no = new String(request.getParameter("trade_no")
						.getBytes("ISO-8859-1"), "UTF-8");
				// 交易状态
				String trade_status = new String(request.getParameter(
						"trade_status").getBytes("ISO-8859-1"), "UTF-8");
				// 支付金额
				String totalFee = new String(request.getParameter(
						"buyer_pay_amount").getBytes("ISO-8859-1"), "UTF-8");
				// 买家账号
				String buyerEmail = new String(request.getParameter(
						"buyer_logon_id").getBytes("ISO-8859-1"), "UTF-8");
				System.out.println(">>>>>>>>>>>>>>>商户订单号:" + out_trade_no);
				System.out.println(">>>>>>>>>>>>>>>>>支付宝交易号" + trade_no);
				System.out.println(">>>>>>>>>>>>>>>>>>>>交易状态" + trade_status);
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>支付金额" + totalFee);
				System.out.println(">>>>>>>>>>>>>>>>>>>>买家账号" + buyerEmail);
                // 验证
				if (flag) {
					if (trade_status.equals("TRADE_FINISHED")
							|| trade_status.equals("TRADE_SUCCESS")) {
							System.out.println(">>>>>>>>>>>>>>>>>>>>>验证确认支付成功！！！！！！！！！！！");








							//终止支付宝支付成功回调
//                        response.getWriter().write("success");
							BufferedOutputStream out=new BufferedOutputStream(response.getOutputStream());
							out.write("success".getBytes());
							out.flush();
							out.close();
						} else {
							System.out.println("fail1");
							response.getWriter().write("fail");
//						return "fail";
						}
					} else {// 验证失败
						System.out.println("fail2");
						response.getWriter().write("fail");
//					return "fail";
					}

			} catch (AlipayApiException e1) {
				System.out.println("fail3");
                response.getWriter().write("fail");
//				return "fail";
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("fail4");
//			return "fail";
		}
	}
}
