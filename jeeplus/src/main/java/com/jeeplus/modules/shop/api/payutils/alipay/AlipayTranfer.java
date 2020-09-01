package com.jeeplus.modules.shop.api.payutils.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.jeeplus.modules.shop.api.payutils.alipay.entity.ALiPayConfig;
import com.jeeplus.modules.shop.api.payutils.wxpay.util.Hutool;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
* @author
* @version
* 类说明
*/
public class AlipayTranfer {
	public static void main(String[] args) {
		/*AlipayTransfer("", 0.01, "18344884358");*/
		/*Double money=100.0;
		DecimalFormat dex = new DecimalFormat("#0.00");
		String payAccount = String.valueOf(dex.format(money));
		System.out.println(payAccount);*/
		try {
			boolean b = AlipayRefundMoney(0.01, "b023dcf1f54c46dfa22955e954c70_18");
			if(b){
				System.out.println("退款成功");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	//配置初始化信息参数
	public static DefaultAlipayClient  alipayclient(){
		DefaultAlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
				ALiPayConfig.appid, ALiPayConfig.private_key, "json",
				ALiPayConfig.input_charset, ALiPayConfig.ali_public_key,
				"RSA2");
		   return alipayClient;
	}

	/**
	 * 支付宝提现
	 * @param realname
	 * @param money
	 * @param alicount
	 * @return
	 */
  public static boolean  AlipayTransfer (String realname,Double money,String alicount){
	  	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	  	Date date = new Date();
	  	String logtDate = sdf.format(date)
				+ (int) (Math.random() * 9)+ (int) (Math.random() * 9);
			 AlipayClient   aliClient=alipayclient();
			 AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
			 request.setBizContent("{" +
			 //参考接口文档参数解释：https://docs.open.alipay.com/api_28/alipay.fund.trans.toaccount.transfer
			 "\"out_biz_no\":\""+logtDate+"\"," +
			 "\"payee_type\":\"ALIPAY_LOGONID\"," +
			 "\"payee_account\":\""+alicount+"\"," +
			 "\"amount\":\""+money+"\"," +
			 "\"payee_real_name\":\""+realname+"\","+
			 "\"remark\":\"提现\"" +
			 "}");
			 AlipayFundTransToaccountTransferResponse response=null;
			try {
				response = aliClient.execute(request);
				 System.err.println(response.getBody());
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
			 if(response.isSuccess()){
				 return true;
			 } else {
				 return false;
			 }
		}


	/**
	 * 支付宝退款
	 *
	 * @param money
	 * @param out_trade_no
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean AlipayRefundMoney(Double money, String out_trade_no) throws AlipayApiException {

		DecimalFormat dex = new DecimalFormat("#0.00");
		String payAccount = String.valueOf(dex.format(money));// 测试使用（可删除）
		Integer randomNumber = new Random().nextInt(900) + 100;
		String out_request_no = Hutool.getTime() + randomNumber;
//		String out_request_no=StringHideUtils.getOrderId("PT");
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALiPayConfig.appid,
				ALiPayConfig.private_key, "json", ALiPayConfig.input_charset, ALiPayConfig.ali_public_key, "RSA2");
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent("{" + "\"out_trade_no\":\"" + out_trade_no + "\"," + // 商户订单号
				// 也可使用支付宝交易号trade_no这个参数，两个必填一个
				"\"out_request_no\":\"" + out_request_no + "\"," + // 部分退款必须
				"\"refund_amount\":\"" + payAccount + "\"," + // 退款金额
				"\"refund_reason\":\"正常退款\"" + "  }");
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		if (response.isSuccess()) {
			return true;
		} else {
			return false;
		}
	}


}
