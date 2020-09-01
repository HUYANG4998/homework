package com.wxtemplate.wxtemplate.api.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.wxtemplate.wxtemplate.api.pay.config.ALiPayConfig;
import com.wxtemplate.wxtemplate.api.service.IMoneyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/aLiPay")
@CrossOrigin
public class Pay {
    @Autowired
    private IMoneyRecordService moneyRecordService;
    private final static String APP_ID = "2021001152629702";
    private final static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJAsqjgNotigYLaby7uE9ygeH5yvi2twtfo4JYuMofnKfxFfD49cP97qWMzJsCiehOHA5Yyn2E4tL3oUlwF+GaEaoTqgPXpKhG3NqUR3bWduoMGvJO6Rg3ZSB6RTRDuJmIK4XXfm+8l055mQK+NgT2TI4ebzCnMSB9cjaXfhq6Dbz9wmc5bOnhRHNJBdkU2Aq6E/ycA+dyXsb65koxq+wtnnxpkJIWPa7hv5jWpahMN6Fm68E5Tf5tlChda3bbqdpoBx/0YuZg0rpQ5VYHev2WAHMibzpHnz28+BBSZtHVwwz+6MipFbxg5z2HPWTG8K0Sk2fsh5wI1MHNhkm7xJAbAgMBAAECggEADFc7B1Ug0b8/4iFJBaOJTGgUgZpdop+EH74rjHBAjG5g1h9C0DnayHGJadA4Ghdv3DE8vZSnj6OamwFlCvqrsGPs7M6AgBjtq0xTR83UD2cwl5yJvL/UFZE/RqQbHApxkclRyeIykEsUn3E/xQA6nDCzEFy1himvonz6G9UY9S8KaRQOApL0VBPutkwPxt9pUmOdMtwrW6gKjUV4//GFss5gW4wY1HFMIaj7EH3j9PWBiLMDzy6XlAu7LPmPspvY6ZI/FJXO2Anu5WWlawhpXcEuxiXfGmrakBI45R49nhvYx3B52A5ShkMv+1fHIn6UnhxCJlMIhQ9Dpu0c25lcoQKBgQD64sXW8LffUZ0guGuC57vbNEPZgRnHhAh9Y9WUDq6oTf0uxlpqjGqkmEN301eyoa4KaaFtPcR4ayPlr85em+c4t5Zo16STvAMN1hTJYKrsZG1uQLiWwmkIBw0eUvtostyH/9ouNknvtJRpwSSBtFfSQoP6BmoLCEVrdbCYz5BnywKBgQCLzcWRxk1EMg37sHHT8hQRQrnSEtUJ3Ua1mw3WYl7Qlnckhzg4QUO6Z8tJf3frkWhOKn/HSfjb5LfZ3vpDTk7nXPNPjqzp6tEFXo8HWlzdctzDxbaVoVDhwPxhcCt/f7sR4sUrXEfvsF6GpJWXjrpfiBM8ACvti4UHO2VKizlO8QKBgQCQ41YOA4as2Z08wESWnO3Ici6c9gnFR2L/xyAZGUGUwTVuRe8BiYYs+CI+mTcUWnsAWHB5fipwwK9Cfn1X9gFap+udMKxDBVleLDxk8AbF7lmkSoxPxIaTtD7HPHUkyebCQ+fkuEWKwWbUjE5g+Z2eN/uv8PRc2rH8lUMffP+QcQKBgD0x+kxgAAxc2LAWQQ0kRtJzYcFpyuUBfeUalQkFkDk/07Eim2JMdygOLPbkHAr6YCzu/3WtlIg0aqGsr+FsUq+FiV+GtXvC9HGkPXlbfZDVqXrsfYqrzSq0fgTsw62QrMMcf3AGedqaWhYXzv9DU6Njff/il1fonXdX8XOa06ARAoGAC6QlHrFNEifmWdv3hDvgnS/P41jwSV5VC8qfSorRaBt9IMXmJ9CNC/R3SxCWBhS8e7liDLCxurjgqfXEaUTI3Yz35fCoe0BencKfa+HewLDz82h1BI3UGRFpR31GBarinUDjnhChm7fovtnt7ra0p0MuuCRu0ctyIY5GdniwrCY=";
    private final static String CHARSET = "UTF-8";
    private final static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiQLKo4DaLYoGC2m8u7hPcoHh+cr4trcLX6OCWLjKH5yn8RXw+PXD/e6ljMybAonoThwOWMp9hOLS96FJcBfhmhGqE6oD16SoRtzalEd21nbqDBryTukYN2UgekU0Q7iZiCuF135vvJdOeZkCvjYE9kyOHm8wpzEgfXI2l34aug28/cJnOWzp4URzSQXZFNgKuhP8nAPncl7G+uZKMavsLZ58aZCSFj2u4b+Y1qWoTDehZuvBOU3+bZQoXWt226naaAcf9GLmYNK6UOVWB3r9lgBzIm86R589vPgQUmbR1cMM/ujIqRW8YOc9hz1kxvCtEpNn7IecCNTBzYZJu8SQGwIDAQAB";

    public static String AppPay(String price, String orderId) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("");// 商品描述，可空
        model.setSubject("充值");// 订单名称，必填
        model.setOutTradeNo(orderId); // 商户订单号，商户网站订单系统中唯一订单号，必填
        model.setTotalAmount(price);//金额
        model.setProductCode("QUICK_MSECURITY_PAY");// 销售产品码 必填
        request.setBizModel(model);
        String url = "http://8cang26ugn.54http.tech/aLiPay/alipay_callback";
        request.setNotifyUrl(url);
        String bodybody = "";
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //就是orderString 可以直接给客户端请求，无需再做处理。
            bodybody = response.getBody();
            return bodybody;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }

    // 回调
    @RequestMapping("/alipay_callback")
    @ResponseBody
    public String callback(HttpServletRequest request) {
        Map<String, String> params = convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        String tradeStatus = params.get("trade_status");
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            String outTradeNo = params.get("out_trade_no");
            moneyRecordService.changeStatus(outTradeNo);

            return "success";

        } else {
            return "fail";
        }

    }

    /**
     * 支付宝提现
     *
     * @param realname
     * @param money
     * @param alicount
     * @return
     */
    public static boolean AlipayTransfer(String realname, Double money, String alicount) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String logtDate = sdf.format(date)
                + (int) (Math.random() * 9) + (int) (Math.random() * 9);
        AlipayClient aliClient = alipayclient();
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                //参考接口文档参数解释：https://docs.open.alipay.com/api_28/alipay.fund.trans.toaccount.transfer
                "\"out_biz_no\":\"" + logtDate + "\"," +
                "\"payee_type\":\"ALIPAY_LOGONID\"," +
                "\"payee_account\":\"" + alicount + "\"," +
                "\"amount\":\"" + money + "\"," +
                "\"payee_real_name\":\"" + realname + "\"," +
                "\"remark\":\"提现\"" +
                "}");
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = aliClient.execute(request);

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            return true;
        } else {
            return false;
        }
    }

    //配置初始化信息参数
    public static DefaultAlipayClient alipayclient() {
        DefaultAlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                ALiPayConfig.appid, ALiPayConfig.PRIVATE_KEY, "json",
                ALiPayConfig.input_charset, ALiPayConfig.PUBLIC_KEY,
                "RSA2");
        return alipayClient;
    }


}
