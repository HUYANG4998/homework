package com.jeeplus.modules.shop.api.ridereval.service.impl;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.api.ridereval.entity.RiderEval;
import com.jeeplus.modules.shop.api.ridereval.mapper.RiderEvalMapper;
import com.jeeplus.modules.shop.api.ridereval.service.RiderEvalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RiderEvalServiceImpl implements RiderEvalService {
    @Resource
    private RiderEvalMapper riderEvalMapper;
    @Override
    public void insertRiderView(RiderEval riderEval) {
        if(riderEval!=null){
            riderEvalMapper.insertRiderView(riderEval);
        }
    }

    @Override
    public RiderEval selectByOrderId(String orderId) {
        RiderEval riderEval=new RiderEval();
        if(!StringUtils.isEmpty(orderId)){
            riderEval=riderEvalMapper.selectByOrderId(orderId);
        }
        return riderEval;
    }
}
