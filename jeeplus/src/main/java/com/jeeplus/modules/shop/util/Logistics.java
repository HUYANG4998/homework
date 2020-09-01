package com.jeeplus.modules.shop.util;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class Logistics {
    public static String getLogistics(String number){
        String host = "https://wuliu.market.alicloudapi.com";
        String path = "/kdi";
        String method = "GET";
        String appcode = "9b104aabfbd24a9ba6aed3b84d8bc469";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("no", number);
        /* querys.put("t", "EMS");*/
        //JDK 1.8示例代码请在这里下载：  http://code.fegine.com/Tools.zip
        String success=null;
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body

            success = EntityUtils.toString(response.getEntity());
            System.out.println(success);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
    public static void main(String[] args){
        String logistics = getLogistics("9861631827571");
        System.out.println(logistics);
    }

}
