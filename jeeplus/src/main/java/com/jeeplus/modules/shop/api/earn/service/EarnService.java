package com.jeeplus.modules.shop.api.earn.service;

import com.jeeplus.modules.shop.api.earn.entity.Earn;

import java.util.List;
import java.util.Map;

public interface EarnService {
    List<Earn> selectEarnByAddtime(String userid, String addtime);

    Map<String,Object> selectPriceAndYesterDay(String userid, String status);

    void insertEarn(Earn earn);
}
