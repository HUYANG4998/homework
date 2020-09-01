package com.wxtemplate.api.alipay.config;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.github.wxpay.sdk.WXPayUtil;
import com.wxtemplate.api.alipay.weixin.ConfigUtil;
import com.wxtemplate.api.alipay.weixin.PayCommonUtil;
import com.wxtemplate.api.alipay.weixin.XMLUtil;
import com.wxtemplate.api.service.IUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class WxPay {

    private static String  payurl ="https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static String notify_url="http://182.92.64.54:7654/wxNotify";
    private static String spbill_create_ip="182.92.64.54";
    //应用ID
    private  static String  appid ="wxc0bf7b1c09c5a857";
    //商户号
    private  static String  mch_id ="1578555191";
    //设备号
    private  static String  device_info ="WEB";
    //key
    private static String key="gqo4Bvtc3YfEGhTfkFZozLLKTP3bgwKV";
    //交易类型
    private static String trade_type="APP";//APP


    @Autowired
    private IUserService userService;
    /**
     * 创建统一下单签名map
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
    public static  SortedMap<Object, Object>  WxOrder(Map<String,Object> data)  {
        //随机字符串
        String nonce_str =RandomUtil.randomString(31);
        int price100 = new BigDecimal(data.get("price").toString()).multiply(new BigDecimal(100)).intValue();
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);
        parameters.put("nonce_str", nonce_str);
        parameters.put("body", data.get("subject").toString());
        parameters.put("out_trade_no", data.get("orderid").toString()); //订单id
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
            parameterMap2.put("timestamp", Long.parseLong(String.valueOf(System.currentTimeMillis()/1000)));
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
        /*UnifiedorderModel unifiedorderModel = new UnifiedorderModel();
        unifiedorderModel.setAppid(appid).setMch_id(mch_id).setNonce_str(nonce_str)
                .setSign_type("MD5").setAttach("wysy").setBody("万源商院-充值")
                .setNotify_url(notify_url).setOut_trade_no(out_trade_no).setSpbill_create_ip(spbill_create_ip)
                .setTotal_fee(total_fee).setTrade_type("APP");

        Map<String, String> params =createUnifiedSign(unifiedorderModel);
        String sign =sign(params,key);
        SortedMap<String, Object> sortedMap = new TreeMap<>(params);
        String xmlStr =getRequestXml(sortedMap,sign);
        byte[] xmlData = xmlStr.getBytes();
        URL url = null;
        SortedMap<String, Object> map = new TreeMap<>();
        try {
            url = new URL(payurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content_Type", "text/xml");
            urlConnection.setRequestProperty("Content-length", String.valueOf(xmlData.length));
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.write(xmlData);
            outputStream.flush();
            outputStream.close();
           map =  WeChatUtils.parseXml(urlConnection.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  map;*/
    }
    /**
     * 微信回调
     */
    @RequestMapping("/wxNotify")
    @ResponseBody
    public void  wxNotify(HttpServletRequest request, HttpServletResponse response)
    {
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
                    userService.changeStatus(out_trade_no.substring(0,out_trade_no.indexOf("_")));
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
        /*try {
            // 读取参数
            // 解析xml成map
            Map<String, String> map = WXPayUtil.xmlToMap(getParam(request));
            String orderNo = map.get("out_trade_no");
            String resultCode = map.get("result_code");
            // 另起线程处理业务
            executorService.execute(() -> {
                // 支付成功
                if (resultCode.equals("SUCCESS")) {
                    // TODO 自己的业务逻辑
                } else {
                    // TODO 自己的业务逻辑
                }
            });
            if (resultCode.equals("SUCCESS")) {
                return setXml("SUCCESS", "OK");
            } else {
                return setXml("fail", "付款失败");
            }
        } catch (Exception e) {
            return setXml("fail", "付款失败");
        }*/



    /**
     * 验证微信回调签名
     * @Description
     * @Author zhaozhenhua
     * @date   2018年5月10日
     * @return
     * @throws Exception
     */
    public static boolean checkWXSign(Map<String, String> receiveMap) {
        String signFromAPIResponse = (String) receiveMap.get("sign");
        if ("".equals(signFromAPIResponse) && null == signFromAPIResponse) {
            System.out.println("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        receiveMap.remove("sign");
        String sign =sign(receiveMap,key);

        if (!sign.equals(signFromAPIResponse)) {
            //签名验不过，表示这个API返回的数据有可能已经被篡改了
            System.out.println("API返回的数据签名验证不通过，有可能被第三方篡改!!! responseSign生成的签名为" + sign);
            return false;
        }

        System.out.println("服务器回包里面的签名是:" + signFromAPIResponse);
        return true;
    }

    /**
     * 微信统一签名
     * @param
     * @return
     */
    public static String sign(Map<String, String> partemter,String mchKey) {
        SortedMap<String, String> sortedMap=new TreeMap<>(partemter);
        StringBuilder toSign = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = sortedMap.get(key);
            if (value!=null && !"".equals(value) && !"sign".equals(key)
                    && !"key".equals(key)) {
                toSign.append(key).append("=").append(value).append("&");
            }
        }

        toSign.append("key=").append(mchKey);
        return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
    }
    // 请求xml组装
         public static String getRequestXml(SortedMap<String, Object> parameters,String sign) {
             StringBuffer sb = new StringBuffer();
             sb.append("<xml>");
             Set es = parameters.entrySet();
             Iterator it = es.iterator();
             while (it.hasNext()) {
                     Map.Entry entry = (Map.Entry) it.next();
                     String key = (String) entry.getKey();
                     // String value = (String) entry.getValue();
                     Object value = entry.getValue();

                     // || "body".equalsIgnoreCase(key) || "sign".equalsIgnoreCase(key)
                     if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key) || "sign".equalsIgnoreCase(key)) {
                             //sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
                         sb.append("<" + key + ">" + value + "</" + key + ">");
                         } else {
                             sb.append("<" + key + ">" + value + "</" + key + ">");
                         }
                 }
             sb.append("<sign >" + sign + "</sign>");
             sb.append("</xml>");
             return sb.toString();
         }


    private String getParam(HttpServletRequest request) throws IOException {
        // 读取参数
        InputStream inputStream;
        StringBuilder sb = new StringBuilder();
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();
        return sb.toString();
    }



    // 通过xml发给微信消息
    private static String setXml(String return_code, String return_msg) {
        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put("return_code", return_code);
        parameters.put("return_msg", return_msg);
        try {
            return WXPayUtil.mapToXml(parameters);
        } catch (Exception e) {
            return "<xml><return_code><![CDATA[" + return_code + "]]>" + "</return_code><return_msg><![CDATA[" + return_msg
                    + "]]></return_msg></xml>";
        }
    }








}
