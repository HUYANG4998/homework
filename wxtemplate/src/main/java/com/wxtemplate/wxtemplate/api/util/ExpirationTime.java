package com.wxtemplate.wxtemplate.api.util;

import cn.hutool.core.date.DateUtil;
import com.wxtemplate.wxtemplate.api.mapper.MoneyTemplateMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 到期时间判断
 */
@Component
public class ExpirationTime {


    public static boolean expirationTimeJudge(int hot,boolean flag){
        boolean success=false;
        if(hot<1&&flag){
            //小于等于到期时间 加时间
            success=true;
        }else if(flag){
            //大于到期时间  由当前时间加时间
            success=true;
        }else if(hot<1){
            //小于到期时间  不做修改
            success=true;
        }
        return success;
    }
    public static String getExpirationTime(int hot,boolean flag,String time,Integer day){
        if(hot<1&&flag){
            //小于等于到期时间 加时间
            time=DateUtil.format(DateUtil.offsetDay(DateUtil.parse(time), day),TimeUtils.yyyyMMddHHmmss);
        }else if(flag){
            //大于到期时间  由当前时间加时间
            time=DateUtil.format(DateUtil.offsetDay(new Date(), day),TimeUtils.yyyyMMddHHmmss);
        }
        return time;
    }
}
