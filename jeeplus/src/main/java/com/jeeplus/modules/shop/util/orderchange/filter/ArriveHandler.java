package com.jeeplus.modules.shop.util.orderchange.filter;

import cn.hutool.core.date.DateUtil;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;

/**
 * 配送中到达客户目的地跳转配送完成状态
 */
public class ArriveHandler implements IOrderHandler<Order> {
    @Override
    public String handleOrder(Order order, IOrderHandleChain<Order> orderhandler) {
        String success=null;
        if(order.getStatus().equals("6")){
            if("3".equals(order.getRiderState())){
                success="配送已完成";
            }else{
                order.setRiderState("3");
                order.setSongTime(DateUtil.now());
                success="配送成功";
            }

        }
        orderhandler.handleOrder(order);
        return success;
    }
}
