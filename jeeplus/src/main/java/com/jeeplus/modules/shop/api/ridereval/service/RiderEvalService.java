package com.jeeplus.modules.shop.api.ridereval.service;

import com.jeeplus.modules.shop.api.ridereval.entity.RiderEval;

public interface RiderEvalService {

    void insertRiderView(RiderEval riderEval);

    RiderEval selectByOrderId(String orderId);
}
