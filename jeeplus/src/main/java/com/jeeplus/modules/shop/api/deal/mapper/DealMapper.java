package com.jeeplus.modules.shop.api.deal.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.api.chat.entity.Chat;
import com.jeeplus.modules.shop.api.deal.entity.Deal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisMapper
public interface DealMapper extends BaseMapper<Deal> {

    List<Deal> selectDeal(String userid);

    List<Map<String, Object>> selectOrderDetailByOrderId(String orderId);

    void updateByDealId(Deal deal);

    Deal selectDealByOrderId(String orderId);

    Deal selectDealById(String dealId);
}
