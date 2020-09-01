package com.jeeplus.modules.shop.api.pay;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jeeplus.core.web.BaseController;

import com.jeeplus.modules.shop.api.payutils.alipay.entity.ALiPayConfig;
import com.jeeplus.modules.shop.api.payutils.wxpay.util.XMLUtil;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.service.OrderService;
import com.jeeplus.modules.shop.ordertotal.service.OrderTotalService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import io.swagger.annotations.Api;

import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author yuan
 * @date 2019/7/22 0022
 */

@Api(value="ApiAliPayController",description="App支付宝支付控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/notify/")
public class ApiNotifyController extends BaseController {


    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderTotalService orderTotalService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StoreService storeService;
    private static  final  String success = "<xml> \n"+
            " <return_code><![CDATA[SUCCESS]]></return_code>\n"+
            " <return_msg><![CDATA[OK]]></return_msg>\n"+
            "</xml>";
    private static  final  String fail = "<xml> \n"+
            " <return_code><![CDATA[FAIL]]></return_code>\n"+
            " <return_msg><![CDATA[FAIL]]></return_msg>\n"+
            "</xml>";



    //支付后异步请求地址   支付订单
    @RequestMapping("alipay/notify_url_pay_order")
    public void notifyUrlPayOrder(HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入回调方法");
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<String, String>();
            @SuppressWarnings("rawtypes")
            Map requestParams = request.getParameterMap();
            for (@SuppressWarnings("rawtypes")
                 Iterator iter=requestParams.keySet().iterator(); iter.hasNext();) {
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

                        /*String[] split=out_trade_no.split("/");
                        String id = split[0];
                        System.out.println("id"+id);*/

                        List<Order> ordernumber=orderService.selectByNumber(out_trade_no);
                        if(ordernumber.size()>0){
                            for (Order order:ordernumber){
                                if(!"1".equals(order.getOrderState())){
                                    //订单交易状态变为已付款
                                    order.setOrderState("1");
                                    //商家状态信息变为待发货
                                    order.setStoreState("0");
                                    order.setPayTime(new Date());
                                    order.setPayType("0");
                                    /*order.setPeiType("0");*/
                                    orderService.save(order);
                                }

                            }

                        }else{
                            Order order = orderService.get(out_trade_no);
                            if(!"1".equals(order.getOrderState())) {
                                order.setOrderState("1");
                                //商家状态信息变为待发货
                                order.setStoreState("0");
                                order.setPayTime(new Date());
                                order.setPayType("0");
                                /*order.setPeiType("0");*/
                                orderService.save(order);
                            }
                        }
//                        Customer customer=customerService.get(id);
//                        Double money=customer.getMoney();
//
//                        Double aDouble=Double.valueOf(totalFee);
//                        BigDecimal b1 = new BigDecimal(aDouble);
//
//                        BigDecimal bigDecimal=new BigDecimal(money);
//
//                        BigDecimal add=bigDecimal.add(b1);
//                        BigDecimal bigDecimal1=add.setScale(2, BigDecimal.ROUND_HALF_DOWN);
//                        customer.setMoney(bigDecimal1.doubleValue());
//                        customerService.save(customer);

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
    //支付后异步请求地址   支付保证金
    @RequestMapping("alipay/notify_url_pay_margin")
    public void notifyUrlPayMargin(HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入回调方法");
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<String, String>();
            @SuppressWarnings("rawtypes")
            Map requestParams = request.getParameterMap();
            for (@SuppressWarnings("rawtypes")
                 Iterator iter=requestParams.keySet().iterator(); iter.hasNext();) {
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
                        Integer num=Integer.valueOf(out_trade_no.split("_")[1]);
                        out_trade_no=out_trade_no.split("_")[0];

                        Store store = storeService.likeUserId(out_trade_no);
                        if(store!=null){
                            if(num==store.getNum()){
                                store.setBaoMoney(String.valueOf(Double.valueOf(store.getBaoMoney())+Double.valueOf(totalFee)));
                                store.setNum(store.getNum()+1);
                                storeService.save(store);
                            }
                        }

//                        Customer customer=customerService.get(id);
//                        Double money=customer.getMoney();
//
//                        Double aDouble=Double.valueOf(totalFee);
//                        BigDecimal b1 = new BigDecimal(aDouble);
//
//                        BigDecimal bigDecimal=new BigDecimal(money);
//
//                        BigDecimal add=bigDecimal.add(b1);
//                        BigDecimal bigDecimal1=add.setScale(2, BigDecimal.ROUND_HALF_DOWN);
//                        customer.setMoney(bigDecimal1.doubleValue());
//                        customerService.save(customer);

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
    /**微信支付订单*/
    @RequestMapping("wxpay/notify_url_weixin_order")
    public void notifyWeixinOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
//                String pay_id=map.get("attach");
//                System.out.println("pay_id"+pay_id);
               String total_fee=map.get("total_fee");
//                System.out.println("fee"+total_fee);
//                //此处开始写 充值之后的逻辑
//                logger.error("微信支付回调成功");
//
//                Customer customer=customerService.get(pay_id);
//                Double money=customer.getMoney();
//
//                Double aDouble=Double.valueOf(total_fee);
//                BigDecimal b1 = new BigDecimal(aDouble);
//                BigDecimal b2 = new BigDecimal(100);
//                double v=b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//                BigDecimal bigDecimal=new BigDecimal(money);
//                BigDecimal bigDecimal1=new BigDecimal(v);
//
//                BigDecimal add=bigDecimal.add(bigDecimal1);
//                BigDecimal bigDecimal11=add.setScale(2, BigDecimal.ROUND_HALF_DOWN);
//
//                customer.setMoney(bigDecimal11.doubleValue());
//                customerService.save(customer);
                List<Order> ordernumber=orderService.selectByNumber(out_trade_no);
                if(ordernumber.size()>0){
                    for (Order order:ordernumber){
                        if(!"1".equals(order.getOrderState())) {
                            //订单交易状态变为已付款
                            order.setOrderState("1");
                            //商家状态信息变为待发货
                            order.setStoreState("0");
                            order.setPayTime(new Date());
                            order.setPayType("1");
                            /*order.setPeiType("0");*/
                            orderService.save(order);
                        }

                    }

                }else{
                    Order order = orderService.get(out_trade_no);
                    if(!"1".equals(order.getOrderState())) {
                        order.setOrderState("1");
                        //商家状态信息变为待发货
                        order.setStoreState("0");
                        order.setPayTime(new Date());
                        order.setPayType("0");
                        /*order.setPeiType("0");*/
                        orderService.save(order);
                    }
                }



                writeMessageToResponse(response, success);
            } else {
                logger.error("微信支付回调失败");
                writeMessageToResponse(response,fail);
            }
        }else{
            writeMessageToResponse(response, fail);
        }

    }
    /**微信支付保证金*/
    @RequestMapping("wxpay/notify_url_weixin_margin")
    public void notifyWeixinMargin(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
//                String pay_id=map.get("attach");
//                System.out.println("pay_id"+pay_id);
                String total_fee=map.get("total_fee");
//                System.out.println("fee"+total_fee);
//                //此处开始写 充值之后的逻辑
//                logger.error("微信支付回调成功");
//
//                Customer customer=customerService.get(pay_id);
//                Double money=customer.getMoney();
//
//                Double aDouble=Double.valueOf(total_fee);
//                BigDecimal b1 = new BigDecimal(aDouble);
//                BigDecimal b2 = new BigDecimal(100);
//                double v=b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//                BigDecimal bigDecimal=new BigDecimal(money);
//                BigDecimal bigDecimal1=new BigDecimal(v);
//
//                BigDecimal add=bigDecimal.add(bigDecimal1);
//                BigDecimal bigDecimal11=add.setScale(2, BigDecimal.ROUND_HALF_DOWN);
//
//                customer.setMoney(bigDecimal11.doubleValue());
//                customerService.save(customer);
                Integer num=Integer.valueOf(out_trade_no.split("_")[1]);
                out_trade_no=out_trade_no.split("_")[0];

                Store store = storeService.likeUserId(out_trade_no);
                if(store!=null){
                    if(num==store.getNum()){
                        store.setBaoMoney(String.valueOf(Double.valueOf(store.getBaoMoney())+Double.valueOf(BigDecimal.valueOf(Long.valueOf("1")).divide(new BigDecimal(100)).toString())));
                        store.setNum(store.getNum()+1);
                        storeService.save(store);
                    }
                }

                writeMessageToResponse(response, success);
            } else {
                logger.error("微信支付回调失败");
                writeMessageToResponse(response,fail);
            }
        }else{
            writeMessageToResponse(response, fail);
        }

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


}
