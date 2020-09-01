package com.wxtemplate.wxtemplate.api.pay.controller;

import cn.hutool.core.util.RandomUtil;
import com.wxtemplate.wxtemplate.api.pay.config.UnifiedorderModel;
import com.wxtemplate.wxtemplate.api.pay.weixin.PayCommonUtil;
import com.wxtemplate.wxtemplate.api.pay.weixin.XMLUtil;
import com.wxtemplate.wxtemplate.api.service.IMoneyRecordService;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class WxPay {

    private static String payurl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 回调
    private static String notify_url = "http://8cang26ugn.54http.tech/wxNotify";
    private static String spbill_create_ip = "192.168.50.108";
    //应用ID
    private static String appid = "wx469c5b401ca20530";
    //商户号
    private static String mch_id = "1570093831";
    //key
    private static String key = "nkGXx1NmYDlS3pSwEplGbZmcirbhq7vD";


    @Autowired
    private IMoneyRecordService moneyRecordService;

    /**
     * 创建统一下单签名map
     *
     * @param request
     * @param
     * @return
     */
    public static Map<String, String> createUnifiedSign(UnifiedorderModel request) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", request.getAppid());
        map.put("attach", request.getAttach());
        map.put("body", request.getBody());
        map.put("mch_id", request.getMch_id());
        map.put("nonce_str", request.getNonce_str());
        map.put("notify_url", request.getNotify_url());
        map.put("out_trade_no", request.getOut_trade_no());
        map.put("spbill_create_ip", request.getSpbill_create_ip());
        map.put("total_fee", String.valueOf(request.getTotal_fee()));
        map.put("trade_type", request.getTrade_type());
        /*map.put("timestamp",System.currentTimeMillis() / 1000 + "");*/
        return map;
    }


    //下订单
    public static SortedMap<Object, Object> WxOrder(String price, String orderId) {
        //随机字符串
        String nonce_str = RandomUtil.randomString(31);
        int price100 = new BigDecimal(price).multiply(new BigDecimal(100)).intValue();
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);
        parameters.put("nonce_str", nonce_str);
        parameters.put("body", "充值");
        parameters.put("out_trade_no", orderId); //订单id
        parameters.put("fee_type", "CNY");
        parameters.put("total_fee", String.valueOf(price100));
        parameters.put("spbill_create_ip", spbill_create_ip);
        parameters.put("notify_url", notify_url);
        parameters.put("trade_type", "APP");
        //设置签名
        String sign = PayCommonUtil.createSign(key, parameters);
        parameters.put("sign", sign);
        //封装请求参数结束
        String requestXML = PayCommonUtil.getRequestXml(parameters);
        //调用统一下单接口
        String result = PayCommonUtil.httpsRequest(payurl, "POST", requestXML);
        /*System.out.println("\n" + result);*/
        SortedMap<Object, Object> parameterMap2 = new TreeMap<Object, Object>();
        try {
            /**统一下单接口返回正常的prepay_id，再按签名规范重新生成签名后，将数据传输给APP。参与签名的字段名为appId，partnerId，prepayId，nonceStr，timeStamp，package。注意：package的值格式为Sign=WXPay**/
            Map<String, String> map = XMLUtil.doXMLParse(result);

            parameterMap2.put("appid", appid);
            parameterMap2.put("partnerid", mch_id);
            parameterMap2.put("prepayid", map.get("prepay_id"));
            parameterMap2.put("package", "Sign=WXPay");
            parameterMap2.put("noncestr", PayCommonUtil.CreateNoncestr());
            //本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
            parameterMap2.put("timestamp", Long.parseLong(String.valueOf(System.currentTimeMillis() / 1000)));
            String sign2 = PayCommonUtil.createSign(key, parameterMap2);
            parameterMap2.put("sign", sign2);//此 parameterMap2内容送到ios调用就可以了
               /* landlordNew.setWxSign(sign2);
                this.landlordNewService.update(landlordNew);*/
            System.out.println(parameterMap2);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parameterMap2;

    }

    /**
     * 微信回调
     */
    @RequestMapping("/wxNotify")
    @ResponseBody
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            //读取参数
            InputStream inputStream;
            StringBuffer sb = new StringBuffer();
            inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
            //解析xml成map
            Map<String, String> m = new HashMap<String, String>();
            m = XMLUtil.doXMLParse(sb.toString());
            for (Object keyValue : m.keySet()) {
                System.out.println(keyValue + "=" + m.get(keyValue));
            }
            //过滤空 设置 TreeMap
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            Iterator it = m.keySet().iterator();
            while (it.hasNext()) {
                String parameter = (String) it.next();
                String parameterValue = m.get(parameter);

                String v = "";
                if (null != parameterValue) {
                    v = parameterValue.trim();
                }
                packageParams.put(parameter, v);
            }
            //判断签名是否正确
            String resXml = "";
            if (PayCommonUtil.isTenpaySign(key, packageParams)) {
                if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                    // 这里是支付成功
                    String out_trade_no = (String) packageParams.get("out_trade_no"); //微信支付订单号
                    moneyRecordService.changeStatus(out_trade_no);
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                }
            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            }
            //------------------------------
            //处理业务完毕
            //------------------------------
            BufferedOutputStream out = new BufferedOutputStream(
                    response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
