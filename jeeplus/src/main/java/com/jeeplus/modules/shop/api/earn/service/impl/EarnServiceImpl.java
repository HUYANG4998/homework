package com.jeeplus.modules.shop.api.earn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.api.earn.entity.Earn;
import com.jeeplus.modules.shop.api.earn.mapper.EarnMapper;
import com.jeeplus.modules.shop.api.earn.service.EarnService;
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
public class EarnServiceImpl implements EarnService {
    @Resource
    private EarnMapper earnMapper;
    @Resource
    private RiderService riderService;
    @Resource
    private StoreService storeService;
    @Override
    public List<Earn> selectEarnByAddtime(String userid, String addtime) {
        List<Earn> list=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            list=earnMapper.selectEarnByAddtime(userid,addtime);
        }
        return list;
    }

    @Override
    public Map<String,Object> selectPriceAndYesterDay(String userid, String status) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(userid) && !StringUtils.isEmpty(status)) {
            if ("1".equals(status)) {
                Store store = storeService.get(userid);
                //商家
                map.put("price", store.getMoney());
            } else if ("2".equals(status)) {
                Rider rider = riderService.get(userid);
                //骑手
                map.put("price", rider.getMoney());
            }
            String day=DateUtil.format(DateUtil.yesterday(),"yyyy-MM-dd");
            String price = earnMapper.yesterdayEarnAndprice(userid, DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd"));
            if (!StringUtils.isEmpty(price)) {
                map.put("yesterDay", price);
            } else {
                map.put("yesterDay", 0);
            }
    }
        return map;
    }

    @Override
    public void insertEarn(Earn earn) {
        if(earn!=null){
            earnMapper.addEarn(earn);
        }
    }
}
