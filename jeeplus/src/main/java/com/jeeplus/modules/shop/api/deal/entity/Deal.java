package com.jeeplus.modules.shop.api.deal.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.shop.util.Utils;

import java.util.Map;
import java.util.List;


public class Deal extends DataEntity<Deal> {

    private static final long serialVersionUID = 1L;
    public Deal(){
        this.dealId= Utils.getUUID();
    }

    private String dealId;

    private String orderId;

    private String status;

    private String isRead;

    private String updatetime;



    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }


}
