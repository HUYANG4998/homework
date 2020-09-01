package com.wxtemplate.api.util;
import com.gexin.rp.sdk.base.IGtAPN;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.Message;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.payload.MultiMedia;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.gexin.rp.sdk.base.impl.AppMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushtoSingle {
    // 详见【概述】-【服务端接入步骤】-【STEP1】说明，获得的应用配置
    private static String appId = "9fiCOEmVuE6Cortmt3aX88";

    private static String appKey = "RyWkgsrUD169yeZSqcgND";
    private static String masterSecret = "ViUmyGj7ViAHBdisHfQQC5";

    // 别名推送方式
    // static String Alias = "";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    public static void main(String[] args){
        Map<String,Object> map=new HashMap<>();
        map.put("cid","66913c0dcf39b7ffc5113fa8ee2e47c6");
        map.put("title","订单已完成");
        map.put("text","订单已完成，请重新验证");
        map.put("version","ios");
        PushtoSingle.push(map);
    }

    @SuppressWarnings("unchecked")
    public static void push(Map<String,Object> map) {

        /*IGtPush push = new IGtPush(host, appKey, masterSecret);

        Style0 style = new Style0();
        // STEP2：设置推送标题、推送内容
        style.setTitle("请输入通知栏标题1");
        style.setText("请输入通知栏内容1"+System.currentTimeMillis());
        style.setLogo("push.png");  // 设置推送图标
        // STEP3：设置响铃、震动等推送效果
        style.setRing(true);  // 设置响铃
        style.setVibrate(true);  // 设置震动


        // STEP4：选择通知模板
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setStyle(style);


        // STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);  // 时间单位为毫秒

        // STEP6：执行推送
        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());*/
        String cid=map.get("cid")==null?null:map.get("cid").toString();
        String title=map.get("title")==null?null:map.get("title").toString();
        String text=map.get("text")==null?null:map.get("text").toString();
        String version=map.get("version")==null?null:map.get("version").toString();
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        if("android".equals(version)) {
            NotificationTemplate template = getNotificationTemplate(title, text);
            /*TransmissionTemplate template = getTemplate(title, text);*/
            SingleMessage message= new SingleMessage();
            message.setOffline(true);
            // 离线有效时间，单位为毫秒
            message.setOfflineExpireTime(24 * 3600 * 1000);
            message.setData(template);
            // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
            message.setPushNetWorkType(0);
            Target target = new Target();
            target.setAppId(appId);
            target.setClientId(cid);
            //target.setAlias(Alias);
            IPushResult ret = null;
            try {

                ret = push.pushMessageToSingle(message, target);
            } catch (RequestException e) {
                e.printStackTrace();
                ret = push.pushMessageToSingle(message, target, e.getRequestId());
            }
            if (ret != null) {
                System.out.println(ret.getResponse().toString());
            } else {
                System.out.println("服务器响应异常");
            }
        }else if("ios".equals(version)){
            TransmissionTemplate template = getTemplate(title, text);
            SingleMessage message= new SingleMessage();
            message.setOffline(true);
            // 离线有效时间，单位为毫秒
            message.setOfflineExpireTime(24 * 3600 * 1000);
            message.setData(template);
            // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
            message.setPushNetWorkType(0);
            Target target = new Target();
            target.setAppId(appId);
            target.setClientId(cid);
            //target.setAlias(Alias);
            IPushResult ret = null;
            try {

                ret = push.pushMessageToSingle(message, target);
            } catch (RequestException e) {
                e.printStackTrace();
                ret = push.pushMessageToSingle(message, target, e.getRequestId());
            }
            if (ret != null) {
                System.out.println(ret.getResponse().toString());
            } else {
                System.out.println("服务器响应异常");
            }
        }




        // IOS通道下发策略
        /*message.setStrategyJson("{\"ios\":4}");*/


}
    public static NotificationTemplate getNotificationTemplate(String title,String text) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setStyle(androidStyle(title,text));

        return template;
    }

    public static TransmissionTemplate getTemplate(String title,String text) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        /*template.setTransmissionContent("透传内容");*/
        /*template.setTransmissionType(2);*/
        /*template.setTransmissionContent("透传内容"); //透传内容*/
        /*template.setTransmissionContent("透传内容");
        template.setTransmissionType(2);
        template.setTransmissionContent("透传内容");*/
        template.setAPNInfo(getAPNPayload(title,text)); //ios消息推送

        return template;
    }

    private static Style0 androidStyle(String title,String text){
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(title);
        style.setText(text);
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        style.setChannel("Default");
        style.setChannelName("Default");
        style.setChannelLevel(3); //设置通知渠道重要性
        return style;
    }
    private static APNPayload getAPNPayload(String title,String text) {
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(0);
        //ios 12.0 以上可以使用 Dictionary 类型的 sound
        payload.setSound("default");
        payload.setCategory("$由客户端定义");
        payload.addCustomMsg("由客户自定义消息key", "由客户自定义消息value");

        //简单模式APNPayload.SimpleMsg
        /*payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));*/
        payload.setAlertMsg(getDictionaryAlertMsg(text,title));  //字典模式使用APNPayload.DictionaryAlertMsg
       /* payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));*/
        //设置语音播报类型，int类型，0.不可用 1.播放body 2.播放自定义文本
        payload.setVoicePlayType(0);
        //设置语音播报内容，String类型，非必须参数，用户自定义播放内容，仅在voicePlayMessage=2时生效
        //注：当"定义类型"=2, "定义内容"为空时则忽略不播放
        payload.setVoicePlayMessage("定义内容");
        /*-------------------*/

        return payload;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String text,String title) {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(text);
        alertMsg.setActionLocKey("显示关闭和查看两个按钮的消息");
        alertMsg.setLocKey("loc-key1");
        alertMsg.addLocArg("loc-ary1");
        alertMsg.setLaunchImage("调用已经在应用程序中绑定的图形文件名");
        // iOS8.2以上版本支持
        alertMsg.setTitle(title);
        alertMsg.setTitleLocKey("自定义通知标题");
        alertMsg.addTitleLocArg("自定义通知标题组");
        return alertMsg;
    }

}
