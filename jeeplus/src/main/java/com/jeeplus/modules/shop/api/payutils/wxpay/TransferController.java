package com.jeeplus.modules.shop.api.payutils.wxpay;

import cn.hutool.core.date.DateUtil;
import com.jeeplus.common.json.AjaxJson;

import com.jeeplus.modules.shop.api.earn.entity.Earn;
import com.jeeplus.modules.shop.api.earn.service.EarnService;
import com.jeeplus.modules.shop.api.payutils.wxpay.entity.WxConstantUtil;
import com.jeeplus.modules.shop.api.payutils.wxpay.tranferutil.CollectionUtil;
import com.jeeplus.modules.shop.api.payutils.wxpay.tranferutil.HttpUtils;
import com.jeeplus.modules.shop.api.payutils.wxpay.tranferutil.PayUtil;
import com.jeeplus.modules.shop.api.payutils.wxpay.tranferutil.XmlUtil;
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.rider.service.RiderService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间：2016年11月9日 下午5:49:00
 *
 * @author andy
 * @version 2.2
 */

@Controller
@RequestMapping("/transfer")
public class TransferController {


	private static final Logger LOG = Logger.getLogger(TransferController.class);
	// 企业付款
	private static final String TRANSFERS_PAY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	// 企业付款查询
	private static final String TRANSFERS_PAY_QUERY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";
	private static final String APP_ID =WxConstantUtil.APP_ID;

	private static final String MCH_ID = WxConstantUtil.MCH_ID;

	private static final String API_SECRET = WxConstantUtil.APP_KEY;
	@Autowired
	private StoreService storeService;
	@Autowired
	private RiderService riderService;
	@Autowired
	private EarnService earnService;

	/**
	 * 企业向个人支付转账
	 * @param request
	 * @param response
	 * @param openid 用户openid
	 * @param user_id
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public AjaxJson transferPay(HttpServletRequest request, HttpServletResponse response, String openid, double amount,  String user_id) {
		LOG.info("[/transfer/pay]");
		AjaxJson j=new AjaxJson();
		//业务判断 openid是否有收款资格

		//根据userId获取 openid 是否为空 为空 返回
		 int am= (int)(amount * 100);
		Map<String, String> restmap = null;
		try {
			Map<String, String> parm = new HashMap<String, String>();
			//公众账号appid
			parm.put("mch_appid", APP_ID);
			//商户号
			parm.put("mchid", MCH_ID);

			//随机字符串
			parm.put("nonce_str", PayUtil.getNonceStr());
			//商户订单号
			parm.put("partner_trade_no", PayUtil.getTransferNo());
			//用户openid
			parm.put("openid", openid);
			//校验用户姓名选项 OPTION_CHECK
			parm.put("check_name", "NO_CHECK");
			//parm.put("re_user_name", "安迪"); //check_name设置为FORCE_CHECK或OPTION_CHECK，则必填
			//转账金额
			parm.put("amount", String.valueOf(am));
			//企业付款描述信息
			parm.put("desc", "提现成功");
			System.out.println("sssss"+PayUtil.getLocalIp(request));
			//Ip地址
			parm.put("spbill_create_ip", PayUtil.getLocalIp(request));
			parm.put("sign", PayUtil.getSign(parm, API_SECRET));

			String restxml = HttpUtils.posts(TRANSFERS_PAY, XmlUtil.xmlFormat(parm, false));
			restmap = XmlUtil.xmlParse(restxml);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		//声明map 集合 返回前台
		Map<String, String> transferMap = new HashMap<>();
		if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
			LOG.info("转账成功：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			//商户转账订单号
			transferMap.put("partner_trade_no", restmap.get("partner_trade_no"));
			//微信订单号
			transferMap.put("payment_no", restmap.get("payment_no"));
			//微信支付成功时间
			transferMap.put("payment_time", restmap.get("payment_time"));

			//业务逻辑 start
			Store store = storeService.get(user_id);
			Rider rider = riderService.get(user_id);

			if(store!=null){
				Double byOne = Double.valueOf(store.getMoney());
				if(byOne>=amount){
					store.setMoney(String.valueOf(byOne-amount));
				}
				storeService.save(store);
			}else if(rider!=null){
				Double money = Double.valueOf(rider.getMoney());
				if(money>=amount){
					rider.setMoney(String.valueOf(money-amount));
				}
				riderService.save(rider);

			}
			Earn earn=new Earn("提现",String.valueOf(amount),user_id,"0", DateUtil.now());
			earnService.insertEarn(earn);
			j.setSuccess(true);
			j.setMsg("提现成功");

			//业务逻辑 end

		}else {
			if (CollectionUtil.isNotEmpty(restmap)) {
				LOG.info("转账失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
				j.setMsg(restmap.get("err_code_des"));
				j.setErrorCode("500");

				return  j;
			}else{
				j.setMsg(restmap.get("err_code_des"));
				j.setErrorCode("500");
				return  j;
			}

//			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
//					.toJSONString(new JsonResult(-1, "转账失败", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}
		j.setMsg("转账失败");
		j.setErrorCode("500");
		return  j;
	}

	/**
	 * 企业向个人转账查询
	 * @param request
	 * @param response
	 * @param tradeno 商户转账订单号
	 * @param callback
	 */
//	@RequestMapping(value = "/pay/query", method = RequestMethod.POST)
//	public void orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeno,
//                              String callback) {
//		LOG.info("[/transfer/pay/query]");
//		if (StringUtil.isEmpty(tradeno)) {
//			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
//					.toJSONString(new JsonResult(-1, "转账订单号不能为空", new ResponseData()), SerializerFeatureUtil.FEATURES)));
//		}
//
//		Map<String, String> restmap = null;
//		try {
//			Map<String, String> parm = new HashMap<String, String>();
//			parm.put("appid", APP_ID);
//			parm.put("mch_id", MCH_ID);
//			parm.put("partner_trade_no", tradeno);
//			parm.put("nonce_str", PayUtil.getNonceStr());
//			parm.put("sign", PayUtil.getSign(parm, API_SECRET));
//
//			String restxml = HttpUtils.posts(TRANSFERS_PAY_QUERY, XmlUtil.xmlFormat(parm, true));
//			restmap = XmlUtil.xmlParse(restxml);
//		} catch (Exception e) {
//			LOG.error(e.getMessage(), e);
//		}
//
//		if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
//			// 订单查询成功 处理业务逻辑
//			LOG.info("订单查询：订单" + restmap.get("partner_trade_no") + "支付成功");
//			Map<String, String> transferMap = new HashMap<>();
//			transferMap.put("partner_trade_no", restmap.get("partner_trade_no"));//商户转账订单号
//			transferMap.put("openid", restmap.get("openid")); //收款微信号
//			transferMap.put("payment_amount", restmap.get("payment_amount")); //转账金额
//			transferMap.put("transfer_time", restmap.get("transfer_time")); //转账时间
//			transferMap.put("desc", restmap.get("desc")); //转账描述
//			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
//					.toJSONString(new JsonResult(1, "订单转账成功", new ResponseData(null, transferMap)), SerializerFeatureUtil.FEATURES)));
//		}else {
//			if (CollectionUtil.isNotEmpty(restmap)) {
//				LOG.info("订单转账失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
//			}
//			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
//					.toJSONString(new JsonResult(-1, "订单转账失败", new ResponseData()), SerializerFeatureUtil.FEATURES)));
//		}
//	}

}
