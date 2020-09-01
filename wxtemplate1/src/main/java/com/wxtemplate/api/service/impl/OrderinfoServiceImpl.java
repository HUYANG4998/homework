package com.wxtemplate.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Orderinfo;
import com.wxtemplate.api.mapper.OrderinfoMapper;
import com.wxtemplate.api.service.IOrderinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 订单详情 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class OrderinfoServiceImpl extends ServiceImpl<OrderinfoMapper, Orderinfo> implements IOrderinfoService {

    @Resource
    private OrderinfoMapper orderinfoMapper;

    @Override
    public List<Map<String, Object>> selectOrderinfoByOrderid(String orderid,String type) {
        List<Map<String,Object>> orderinofMap=new ArrayList<>();
        if(!StringUtils.isEmpty(orderid)&&!StringUtils.isEmpty(type)){
            orderinofMap=orderinfoMapper.selectCompleteOrderinfoByOrderid(orderid,type);
        }
        return orderinofMap;
    }

    @Override
    public Map<String, Object> selectOrderinfoByOrderinfoid(String orderinfoid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(orderinfoid)){
            map=orderinfoMapper.selectOrderinfoByOrderinfoid(orderinfoid);
            if(map.containsKey("imgurl")){
                String imgurl= map.get("imgurl")==null?null:map.get("imgurl").toString();
                if(!StringUtils.isEmpty(imgurl)){
                    List<String> imgurlList = Arrays.asList(imgurl.split(";"));
                    if(imgurlList!=null){
                        map.put("imgurl",imgurlList);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public void updateOrderinfo(Orderinfo orderinfo) {
        if(orderinfo!=null){
            orderinfoMapper.updateById(orderinfo);
        }
    }
}
