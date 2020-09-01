package com.wxtemplate.api.alipay.controller;

import cn.hutool.core.codec.Base64;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppQueryRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppQueryResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;

import com.wxtemplate.api.entity.Orders;
import com.wxtemplate.api.service.IUserService;
import com.wxtemplate.api.util.JSONUtils;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/aLiPay")
@CrossOrigin
public class Pay {
    @Autowired
    private IUserService userService;
    private static Map<String,String> map1 = new HashMap<>();
    private static String APP_ID="2021001140613275";
   // private static String APP_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCmvA/cB7hzLVb3z/s8MDuTfpfEKbGRU8abjOzJPTt906481Kr4+FWVTproxXkuRXXF4W1NL83AEDGLj2mTK+jpq2Jre0iCb+AzN9pD4oKynnsL+SA/scbggowHmbQK6dJUSqJUMnj1lau5ODwS7b27CShHsuFqyl0nhEnMucimFkP0wukqSpqBTHQKeok7QGAp+LMs3SaUxI1ii3CmPztlUqACcCdp+PzNeAx33xCHDP8Pv+e0mOAVtIL+Afku2yPyM604sFTkk1GDLlNZtuUb/CrwD9w5WVl0Zjy8cmE8Ik3NR2l908r5mmFsNusezCTfkGwXZlLF3nkwJvF98jfpAgMBAAECggEAcqLMeKhqPe5HZByADNLQs0kPiZRs/JFP9PTAfF/4PjvDCRni70i2dvBTe4UCGzkwjzu9jZs3aDz6A403tjMzcS0ffuyFXwESccSN8ayT1cE2snW3oXNWXFgFdNhNvE8lCEVENIV7OsaMCc3LJ+yDTUwvgJRRfb3irF+86eA2P1Hkox3Pl7ul/HyzU6/NrrF8BbreuyyDGiiEM8mwzOC7LhVq8k9/JEJtapCY4dAH5CBb/m0Fa/v4YVog4Tp8OsyBqwPvOgbBVeW82tAV4k3QQfshpElTkSt0nwa0Ic4PdeS6FEmLL7/+3YJcvl6Fu65pn09mSp1kOH3rI51gzTeG8QKBgQDf/VHnypvxJoScGJYfbmO/VlC0XQcAHdO+ho0Ui7hxCN/q+qRpU2iYQMTpcHGzAzXccUWQ6QB8I272FfqK8RdPvQv6S3uIvqv7byg9DpQgBV8CevI2f2JtDX8AjvLj2PNrZQkxLtKV3cz9AgqSoC/8On51xjHOlGYZvOpfxNzD1QKBgQC+kBCpQdkAUF+/ztTSQaGq+P1Vae7OXr1pZI2kcTGhi/VkNqjYNQTdN76/RZ9XRqCKpb6OlNF7K5a+GCBC+EVlrwHWIQbZfuifJOL6PfTjmU1GARhmB2tjcrgEBnFUg/JqwBSLPSn5VkTKvS7oTVEmkpmElggrAbSe9b30uuTxxQKBgQDM9n82fb5I4xhBxnXI+TipY5CiGuZHAwQ5ueHrRRdXx+kumoBMKRjzCYGmo1BHoM5uk/A2dCAPeFUXNnF3JrpQKwuE1AGM1f2GLOdDR83fp0qTeHGf0Nz+MXVfy28Tc/j68w+AQ/pLTRVBB9co6RA/3D7QArac9Eqhjq9hR0FEqQKBgB2q2h0vtjP5T17SOYneU+gFSZNxoY8Zas2srOyL/tkSrAqFA/ZdErvKAPVQRoVU5ouPUEJsYsyVNau0n9ktQ8iX3WZTz/gubNLR8FBbMvP4GvKqGv1PbGWYJ5Pg2OX6gNQjwU8IH6/EwYL6vwHFf7vMmP/uR6lCY7dXtNIUAq6tAoGASXnZEsLbJOGDLbJ1o+0J/iQ8DkNThZsKCXlwc6ZQDZ3eQ2+QBdrL875Jj1XSSETM+Rc5U3O9SPlBYRg3U8kgK6jY7gU0mRqQHcrSXxM5wWFgMhGm5M/zjdgsYPIHGr4YU94giDxwbzFOHwE6GOli7gWISR2YaNb490iYEbE9VDU=";
   private static String APP_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDtwRL3KHWYdIe3YsmReJmQNqPLzjFNHF4BAkKz800khfBjnxYlBgGKchmPTPbketsYlDDDkv3oVf6SY7oHqL7MUmgwkOLuMkgz0KpoUjub/P254s0dQ8x2hNTDvZQQg3SQ4HJ38tZxw5jp+7t7PS4QqPQ/4jFvwta29CPpLQSaqPyko7tCInK/EyRyEmLCiQLmMKyJSFMyCxWH+qe+kAnX28psvC/mCH+jgMtTJN+p++FKI8Flkbbf5aPY7XWSPHp0ixBPlPNWSHWmfJ+WfHLVItegXd629q3kw5ya+sa1GEBy9803YElGg16lz3LqEz/dosSyS3LEL4tVI5o88PCBAgMBAAECggEAP6rqd0FgbFkYVuWfcNNI3DOlnVg0H6AvFDoUvreyussbWrIpbfSzyO1RJuzHWNwFfAmDHXxpfiQ5Ld6C3LY1keWnfQnvmzXf2/BPEeyjhdwz3el8YA/d2wcrKtfDpMBvILTaMoDXKFPmyRHeXNw/T7rO3aSm9HTKpueuDi0+BGQkRDB+wSgxbEzQ+wvg2JEGVs9tctmLEhPMeK+s62+p1vFUFgwGGnWbKyimHeiEGu8tjjlzNxZC+KlcoKkRLyrTlsJa+WH7S8S4rwFp/ioVlRy2E0dajMMty0cV2K/mUtkc+XDKDGMm1wyfd9CX0WpFlzup2bz0wkjS0SnTJsfSoQKBgQD9xq7ZzvKrTIqBM+0YS2vtlKiWij8gccrQx0qYG6QCU3nH5kQr7PUqCq5JW7erq5JE/IO6Ny7LKtgwLi730Cw8OQahBaDRLfuAowcIBblvm7CFPUdeAQ1yXOZef5LZ9Pq+FxnwE7jdnOAYq7tOa8Xj87QHwT0ME25e7fGZtehP5QKBgQDv1nKileO+ZXXcOJ6J6WscJcY46chvABmMBGXgvOhGyUY4rlGP8RYNjkOYifUQs5ecSGNpwGiqyDcBc4XHl++0CtO7o/hTFeRGYhkCUYqBtFn5O2t9D+qJiObTCuW1GumNpBq6UVoQHMPwwPwUXDJbzehN08/2RAeroVROS8R8bQKBgHFnJZ+DUQGnVIVkFOE0Yuhp8+RBpu/yqbUeOYE7aND5SIRBq9kNLIk6+KMjbzIxm01koeDG5Aw5+x2cpbRrvDi1Kvctsq2HJMpZD71QAYuUQQ9pUMj7I20lgnRcAd2QEbR8UkTsK0Is5h9gLnVex7GTp7GHcqZA0KNvJPWRSlzZAoGBAMe9XTMyib0jWojRTpekoeOLQg5cBOUMvDnz37TPHtL1BVpnMt4h9i/L71e2bTY/m2A4mTKVX5X7sHOw9hVH4AWdjsCSyljHvpkvUYywxB1JSjVa2yYIf1eTgEj6plux0hh4Nxo8bjlE+5oh3s5GClqSnYlTgbqi5ZB2h3O9fiHxAoGAUciPj6T2HPoY9qaC9sfVvicbRYoUIBkHf8tcjykZjzLp/ok53Z4KV+kWKLqqZyHRQPstNxeOPFgQiDbzu3tkyp5RF8Yc2CGHXlif1oi1RqUmObeplMS/1fSjL5kSJlnQRuiIID46vO/a3aA4d7TAVDtEgdG2uunGHbvamoR+Ouo=";
   private static String CHARSET="UTF-8";
    //private static String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAprwP3Ae4cy1W98/7PDA7k36XxCmxkVPGm4zsyT07fdOuPNSq+PhVlU6a6MV5LkV1xeFtTS/NwBAxi49pkyvo6atia3tIgm/gMzfaQ+KCsp57C/kgP7HG4IKMB5m0CunSVEqiVDJ49ZWruTg8Eu29uwkoR7LhaspdJ4RJzLnIphZD9MLpKkqagUx0CnqJO0BgKfizLN0mlMSNYotwpj87ZVKgAnAnafj8zXgMd98Qhwz/D7/ntJjgFbSC/gH5Ltsj8jOtOLBU5JNRgy5TWbblG/wq8A/cOVlZdGY8vHJhPCJNzUdpfdPK+ZphbDbrHswk35BsF2ZSxd55MCbxffI36QIDAQAB";
    private static String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhEytccJEGboHO/1+i6lO8aMIar1po8Nvyss+FCp+Qya8yBvLxvOgja2YsF/SARNOKkvobi6Haf6TJxmTCistbR8MjareF5IYATwBJIQCo002p2ntrDT02toi85nRxDXRouUG8XBY1AS2D2heJ2tdqe01uW7EE5vPFdHzFtCLeV0oXAJ69vq2uduoPKCdLJhb2MGHiC5fM7rczOv5w4ELJgbBN5EwQl1CSVlKB8L7CRXJgiv5YsB4i3oiDHc7k/6dyRbWqTx1LWZQQvNgpQ9bBIQ96t6O0gc93oTrZ7Px7AK1yN+jUYD5DoDLURG4EpJNdqGn3JMviwzO4blfSmV7BwIDAQAB";

    //MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoPqjpROErP+QafQMMI7h7S39j9pZhQt6KLwAfkInZr1/5jat3QnGgkIXAspBCakFe0g3j3J7hvBrnFuPXAgCQNK8JR1YFOWBt0tpYxILKjBn3T0bYpH0pg5ncnA++Wg3B4EszT3eoF4MFzJOGymhJpXzEHc7bPF79XWhdvpZGLMhg2Ue8KhSPGplaoGIYIeB6vkDWIY0/VBBGldpRKaAAgiOYqcGpVxvx2mdQIiHsd6Dje57AedwqkwaFN6yBkzo1ErMSsLpXxHxtYXh8iS67q8yXRl4T+obXXRXLxEM8JCd+PrhaQb051fKDobqT6gJzZjoK/ijkCjDrB/psIGTIwIDAQAB
   public static String AppPay(Map<String,Object> map){
       /*EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);*/
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
       //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
       //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("");// 商品描述，可空
        model.setSubject(map.get("subject").toString());// 订单名称，必填
        model.setOutTradeNo(map.get("orderid").toString()); // 商户订单号，商户网站订单系统中唯一订单号，必填
        /*model.setTimeoutExpress(); */// 超时时间 可空
        model.setTotalAmount(map.get("price").toString());//金额
        /*model.setPassbackParams(URLEncoder.encode("cc"));*/
        model.setProductCode("QUICK_MSECURITY_PAY");// 销售产品码 必填
        request.setBizModel(model);
//        String s = map1.get("1");
//       request.putOtherTextParam("app_auth_token", s);
       String url ="http://182.92.64.54:7654/aLiPay/alipay_callback";
        request.setNotifyUrl(url);
        String bodybody="";
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //就是orderString 可以直接给客户端请求，无需再做处理。
             bodybody= response.getBody();
            System.out.println(JSON.toJSONString(bodybody));
            return  bodybody;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "";
    }
    // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

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


    @RequestMapping("/alipay_callback")
    @ResponseBody
    public String callback(HttpServletRequest request) {
        Map<String, String> params = convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        String trade_status = params.get("trade_status");
        if("TRADE_SUCCESS".equals(trade_status)){
            String out_trade_no = params.get("out_trade_no");
            String total_amount = params.get("total_amount");
            userService.changeStatus(out_trade_no.substring(0,out_trade_no.indexOf("_")));
           /* Userbillrecord userbillrecord = new Userbillrecord();
            Optional<Userbillrecord> optional = Optional.ofNullable(userbillrecord.setId(out_trade_no).selectById());
            if(optional.isPresent()){
                Userbillrecord userbillrecord1 = optional.get();
                double balance = userbillrecord1.getBalance();

                if(NumberUtil.compare(balance,Double.valueOf(total_amount))==0 && "CZ0".equals(userbillrecord1.getType())){
                    userbillrecord1.setType("CZ1");
                    userbalanceService.CZ1(userbillrecord1);

                }
            }*/

            return "success";

        }else{
            return "fail";
        }

    }
    private String miyao ="ETnprMbguLUQTOj3NY97sQ==";
    @GetMapping("/Callback")
    public Result alipayBusinessLogin(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {
        String appId = request.getParameter("app_id");
        String source = request.getParameter("source");
        String state = request.getParameter("state");
        String userid = Base64.decodeStr(state);

        String appAuthCode = request.getParameter("app_auth_code");
        // 使用app_auth_code换取app_auth_token
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", // 支付宝网关（固定）
                APP_ID, // APPID 即创建应用后生成
                APP_PRIVATE_KEY, // 开发者私钥，由开发者自己生成
                "json", // 参数返回格式，只支持json
                CHARSET, // 编码集，支持GBK/UTF-8
                ALIPAY_PUBLIC_KEY, // 支付宝公钥，由支付宝生成
                "RSA2"); // 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
        AlipayOpenAuthTokenAppRequest aoataRequest = new AlipayOpenAuthTokenAppRequest();
        // 设置请求参数 使用app_auth_code换取app_auth_token
        aoataRequest.setBizContent("{\"grant_type\":\"authorization_code\",\"code\":\"" + appAuthCode + "\"}");
        // 发送请求得到响应
        AlipayOpenAuthTokenAppResponse aoataResponse = alipayClient.execute(aoataRequest);
        if (!aoataResponse.isSuccess()) {
            throw new RuntimeException("获取app_auth_token失败！" + aoataResponse.getSubMsg());
        }
        //{"alipay_open_auth_token_app_response":{"code":"10000","msg":"Success","tokens":[{"app_auth_token":"201912BB816db31487d74374a493209db2f32X87","app_refresh_token":"201912BBe337978b24ed4091bea6eab51bef6A87","auth_app_id":"2021001102690024","expires_in":31536000,"re_expires_in":32140800,"user_id":"2088612209050876"}]},"sign":"AEOSQqmVCuvgReAhmGVkaw+s8qdx81y5jax+hWkQeH8QUPqSa+miVQmIJt3GloLO9klIw2kdehiRb4eXdRHwxgPh50sRfgblD/fWOoooUwnXETZt/bGcCEDeV//LjzNIpVwefx6o2MVPnlAS7+EdsDpC35kacSHK/2EDKMeWTt6u+IAF4tIEsTiAZfKHsK4lbMDXEXxNT+hxrEQCDzOhJtqasNlmGSoOvg30dEmC3Cmi2SDiLH73O2Y2x9Q1Vi5WYo0WPsiNYJjtIOgIAD4E/jiBKksasO3+m+smXNzPy8GCmCqNyLoPkDmP+nAkrUhV6K8VqXgFCzm6T5wfXNGjvg=="}
        String body1 = aoataResponse.getBody();
        JSONObject jsonObject = JSONUtil.parseObj(body1);
        JSONObject jsonObject1 = (JSONObject) jsonObject.get("alipay_open_auth_token_app_response");
        List list = (List) jsonObject1.get("tokens");
        Map<String,String > map = (Map<String, String>) list.get(0);
        String app_auth_token = map.get("app_auth_token");
        map1.put("1",app_auth_token);
        //JSONObject jsonObject1 = JSONUtil.parseObj(alipay_open);
        //String token = jsonObject1.get("tokens").toString();

        //JSONObject tokenss = JSONUtil.parseObj(token);
        String appAuthToken = aoataResponse.getAppAuthToken();
        // 根据appAuthToken换取用户信息
        AlipayOpenAuthTokenAppQueryRequest aoataqRequest = new AlipayOpenAuthTokenAppQueryRequest();
        aoataqRequest.setBizContent("{\"app_auth_token\":\"" + app_auth_token + "\"}");
        AlipayOpenAuthTokenAppQueryResponse appQueryResponse = alipayClient.execute(aoataqRequest);
        if (!appQueryResponse.isSuccess()) {
            throw new RuntimeException("获取用户授权信息失败！" + appQueryResponse.getSubMsg());
        }
        // 用户授权成功 获取授权信息
        String userId = appQueryResponse.getUserId();
        String appID = appQueryResponse.getAuthAppId();
        String body = appQueryResponse.getBody();
        System.out.println(userId);
        System.out.println(appID);
        System.out.println(body);
        try {
            response.sendRedirect("https://www.baidu.com/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 机构从平台提现，付款到机构账户
     *
     * @param
     * @param money
     * @param orderNo
     * @return
     * @throws Exception
     */
    public static String payToAccount(String account, String money,
                                      String orderNo) throws Exception {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");

        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setAmount(money);// 单位（元）
        model.setOutBizNo(orderNo);// 订单号
        model.setPayeeAccount(account);// 账号
        // model.setPayeeRealName("");
        model.setPayeeType("ALIPAY_LOGONID");
        model.setPayerShowName("蚁创教育");
        model.setRemark("蚁创教育云平台提现");
        request.setBizModel(model);
        AlipayFundTransToaccountTransferResponse response = alipayClient
                .execute(request);
        if (response == null || !response.isSuccess()) {

            return null;
        }

        return response.getOrderId();
    }
}
