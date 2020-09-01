package com.jeeplus.modules.shop.api.deal.service;

import com.jeeplus.modules.shop.api.deal.entity.Deal;
import com.jeeplus.modules.shop.api.deal.entity.DealSon;

import java.util.List;
import java.util.Map;

public interface DealService {

    List<DealSon> selectDeal(String userid);

    void updateByDealId(String dealId);

    Deal selectDealByOrderId(String orderId);

    Deal selectDealById(String dealId);
}
