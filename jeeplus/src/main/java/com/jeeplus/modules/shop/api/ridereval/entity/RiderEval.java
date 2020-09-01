package com.jeeplus.modules.shop.api.ridereval.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.util.Utils;

public class RiderEval  extends DataEntity<RiderEval> {
    private static final long serialVersionUID = 1L;
    public RiderEval(){
        this.riderReviewId= Utils.getUUID();
    }
    /**评论id*/
    private String riderReviewId;

    /**骑手id*/
    private String riderId;

    /**用户id*/
    private String customerId;

    /**订单id*/
    private String orderId;

    /**星级*/
    private String star;

    /**是否匿名*/
    private String yesNo;

    /**内容*/
    private String content;

    public String getRiderReviewId() {
        return riderReviewId;
    }

    public void setRiderReviewId(String riderReviewId) {
        this.riderReviewId = riderReviewId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getYesNo() {
        return yesNo;
    }

    public void setYesNo(String yesNo) {
        this.yesNo = yesNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
