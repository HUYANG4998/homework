package com.jeeplus.modules.shop.api.earn.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.shop.util.Utils;

import java.io.Serializable;

public class Earn extends DataEntity<Earn> {

    private static final long serialVersionUID = 1L;

    public Earn(String content,String price,String userid,String earn,String addtime){

        earnid= Utils.getUUID();
        this.content=content;
        this.price=price;
        this.userId=userid;
        this.earn=earn;
        this.addtime=addtime;

    }
    public Earn(){ }

    /**
     * 收益明细id
     */
    private String earnid;

    /**
     * 收益内容
     */
    private String content;

    /**
     * 金额
     */
    private String price;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 加0减1
     */
    private String earn;

    /**
     * 添加时间
     */
    private String addtime;

    public String getEarnid() {
        return earnid;
    }

    public void setEarnid(String earnid) {
        this.earnid = earnid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserid() {
        return userId;
    }

    public void setUserid(String userid) {
        this.userId = userid;
    }

    public String getEarn() {
        return earn;
    }

    public void setEarn(String earn) {
        this.earn = earn;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
