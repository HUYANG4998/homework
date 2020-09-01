package com.wxtemplate;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.gexin.rp.sdk.base.impl.Target;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class WxtemplateApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("123");
    }
    // 详见【概述】-【服务端接入步骤】-【STEP1】说明，获得的应用配置
    private static String appId = "9fiCOEmVuE6Cortmt3aX88";

    private static String appKey = "RyWkgsrUD169yeZSqcgND";
    private static String masterSecret = "ViUmyGj7ViAHBdisHfQQC5";

    // 别名推送方式
    // static String Alias = "";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";
    /**
     * 单个用户ios推送
     *
     * @param map
     * @return
     */
    public static String sendSingleIos(Map<String, Object> map) {
        IGtPush push = new IGtPush(host, appKey, masterSecret);

        SingleMessage message = new SingleMessage();

        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(map.get("payload").toString());
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(1);
        //ios 12.0 以上可以使用 Dictionary 类型的 sound
        payload.setSound("default");
        payload.setCategory("$由客户端定义");
        //简单模式APNPayload.SimpleMsg
        //payload.setAlertMsg(new APNPayload.SimpleAlertMsg(""+map.get("content")));
        //字典模式使用下者
        payload.setAlertMsg(getDictionaryAlertMsg(map));
        template.setAPNInfo(payload);

        message.setData(template);

        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(map.get("cid").toString());
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            return ret.getResponse().toString();
        } else {
            return "";
        }
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(Map<String,Object> map) {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(map.get("body").toString());
        alertMsg.setLocKey("" + map.get("content"));
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        // IOS8.2以上版本支持
        alertMsg.setTitle("" + map.get("title"));
        alertMsg.setTitleLocKey("" + map.get("title"));
        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }
    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", "通知");
        map.put("content", "您有新的订单");
        map.put("cid", "66913c0dcf39b7ffc5113fa8ee2e47c6");
        map.put("payload", "{title:\"通知标题\",content:\"通知内容\"}");
        map.put("body", "您有新的订单");
        String s = sendSingleIos(map);
        System.out.println(s);
    }


}
