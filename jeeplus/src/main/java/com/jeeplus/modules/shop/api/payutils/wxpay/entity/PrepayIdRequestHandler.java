package com.jeeplus.modules.shop.api.payutils.wxpay.entity;



import com.jeeplus.modules.shop.api.payutils.wxpay.util.Md5Util;
import com.jeeplus.modules.shop.api.payutils.wxpay.util.XMLUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class PrepayIdRequestHandler extends RequestHandler {

    public PrepayIdRequestHandler(HttpServletRequest request,
                                  HttpServletResponse response) {
        super(request, response);
    }

    public static String createSign(String charSet, SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + WxConstantUtil.APP_KEY);
        String sign = Md5Util.MD5Encode(sb.toString(),charSet).toUpperCase();
        return sign;
    }
    public String createMD5Sign() {
        StringBuffer sb = new StringBuffer();
        Set es = super.getAllParameters().entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + v + "&");
        }
        String params=sb.append("key="+WxConstantUtil.APP_KEY).substring(0);
        String sign = Md5Util.MD5Encode(params, "utf8");
        return sign.toUpperCase();
    }
    // 提交预支付
    public String sendPrepay() throws Exception {
        String prepayid = "";
        Set es=super.getAllParameters().entrySet();
        Iterator it=es.iterator();
        StringBuffer sb = new StringBuffer("<xml>");
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append("<"+k+">"+v+"</"+k+">");
        }
        sb.append("</xml>");
        String params=sb.substring(0);
        System.out.println("请求参数："+params);
        String requestUrl = super.getGateUrl();
        System.out.println("请求url："+requestUrl);
        TenpayHttpClient httpClient = new TenpayHttpClient();
        httpClient.setReqContent(requestUrl);
        String resContent = "";
        if (httpClient.callHttpPost(requestUrl, params)) {
            resContent = httpClient.getResContent();
            System.out.println("获取prepayid的返回值："+resContent);
            Map<String,String> map=XMLUtil.doXMLParse(resContent);
            if(map.containsKey("prepay_id")){
                prepayid=map.get("prepay_id");
            }

        }
        return prepayid;
    }
}
