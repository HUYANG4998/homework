package com.jeeplus.modules.shop.api.deal.service.impl;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.api.chat.entity.Chat;
import com.jeeplus.modules.shop.api.chat.mapper.ChatMapper;
import com.jeeplus.modules.shop.api.chat.service.ChatService;
import com.jeeplus.modules.shop.api.deal.entity.Deal;
import com.jeeplus.modules.shop.api.deal.entity.DealSon;
import com.jeeplus.modules.shop.api.deal.mapper.DealMapper;
import com.jeeplus.modules.shop.api.deal.service.DealService;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.rider.service.RiderService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DealServiceImpl implements DealService {

    @Resource
    private DealMapper dealMapper;

    @Override
    public List<DealSon> selectDeal(String userid) {
        List<Deal> dealList=new ArrayList<>();
        List<DealSon> dealSonList=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            dealList=dealMapper.selectDeal(userid);

            for (Deal deal:dealList){
                DealSon son=new DealSon();
                son.setDeal(deal);
                son.setMap(dealMapper.selectOrderDetailByOrderId(deal.getOrderId()));
                dealSonList.add(son);
            }
        }
        return dealSonList;
    }

    @Override
    public void updateByDealId(String dealId) {
        if(!StringUtils.isEmpty(dealId)){
            Deal deal=new Deal();
            deal.setDealId(dealId);
            deal.setIsRead("1");
            dealMapper.updateByDealId(deal);
        }
    }

    @Override
    public Deal selectDealByOrderId(String orderId) {
        Deal deal=new Deal();
        if(!StringUtils.isEmpty(orderId)){
            deal=dealMapper.selectDealByOrderId(orderId);
        }
        return deal;
    }

    @Override
    public Deal selectDealById(String dealId) {
        Deal deal=null;
        if(!StringUtils.isEmpty(dealId)){
            deal=dealMapper.selectDealById(dealId);
        }
        return deal;
    }
}
