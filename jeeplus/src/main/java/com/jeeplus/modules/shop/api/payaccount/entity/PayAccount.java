package com.jeeplus.modules.shop.api.payaccount.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.shop.api.earn.entity.Earn;
import com.jeeplus.modules.shop.util.Utils;

public class PayAccount extends DataEntity<Earn> {
    private static final long serialVersionUID = 1L;

    public PayAccount(){
        this.payId= Utils.getUUID();
    }
    private String payId;

    private String userId;

    private String status;

    private String account;

    private String image;

    private String name;

    private String addtime;

    private String updatetime;

    private String openId;

    private String aliUserId;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAliUserId() {
        return aliUserId;
    }

    public void setAliUserId(String aliUserId) {
        this.aliUserId = aliUserId;
    }
}
