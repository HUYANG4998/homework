package com.jeeplus.modules.shop.api.systemutil;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lhh
 * @date 2019/12/20 0020
 */
public class MapUtils {
    private static String API="http://restapi.amap.com/v3/geocode/geo?key=<key>&s=rsv3&address=<address>";

    private static String KEY="389880a06e3f893ea46036f030c94700";

    private static Pattern pattern=Pattern.compile("\"location\":\"(\\d+\\.\\d+),(\\d+\\.\\d+)\"");

    static {
        init();
    }

    private static void init() {
//        System.out.println("高德地图工具类初始化");
//        System.out.println("api: {}"+API);
//        System.out.println("key: {}"+KEY);
        API=API.replaceAll("<key>", KEY);
    }

    public static double[] getLatAndLonByAddress(String address) {
        try {
            String requestUrl=API.replaceAll("<address>", URLEncoder.encode(address, "UTF-8"));
            RequestResult requestResult=RequestUtils.getJsonText(requestUrl, null);
            if (200 != requestResult.getCode()) {
                return null;
            }
            requestUrl=requestResult.getBody();
            if (requestUrl != null) {
                Matcher matcher=pattern.matcher(requestUrl);
                if (matcher.find() && matcher.groupCount() == 2) {
                    double[] gps=new double[2];
                    gps[0]=Double.valueOf(matcher.group(1));
                    gps[1]=Double.valueOf(matcher.group(2));
                    return gps;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        double[] aaa=MapUtils.getLatAndLonByAddress("上海市");
        for (double cccc : aaa) {
            System.out.println(cccc);
        }
        System.out.println(MapUtils.getLatAndLonByAddress("广东省深圳市福田区天安数码城创业科技大厦一期"));

    }
}
