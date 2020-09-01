package com.wxtemplate.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class Tool {

    //返回值
    public static String WSResult(Boolean TF,String ms,Object json,String code){
        JSONObject js = JSONUtil.createObj();
        js.put("success",TF);
        js.put("msg",ms);
        js.put("code",code);
        js.put("data",json);
        String str = js.toString();
        return str;
    }

}
