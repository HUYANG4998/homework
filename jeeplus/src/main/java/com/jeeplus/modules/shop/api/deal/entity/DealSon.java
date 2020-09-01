package com.jeeplus.modules.shop.api.deal.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.shop.util.Utils;

import java.io.Serializable;
import java.util.Map;
import java.util.List;


public class DealSon implements Serializable {

    private static final long serialVersionUID = 1L;
    private Deal deal;
    private List<Map<String,Object>> map;

    public List<Map<String, Object>> getMap() {
        return map;
    }

    public void setMap(List<Map<String, Object>> map) {
        this.map = map;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }
}
