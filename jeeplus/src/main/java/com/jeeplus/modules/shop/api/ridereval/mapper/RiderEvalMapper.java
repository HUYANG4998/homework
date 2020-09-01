package com.jeeplus.modules.shop.api.ridereval.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.api.ridereval.entity.RiderEval;


@MyBatisMapper
public interface RiderEvalMapper extends BaseMapper<RiderEval> {

    void insertRiderView(RiderEval riderEval);

    RiderEval selectByOrderId(String orderId);
}
