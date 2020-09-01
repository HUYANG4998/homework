package com.jeeplus.modules.shop.util.orderchange.filter;

import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;

/**
 * 骑手拒绝时间到达跳到待取货状态
 */
public class PickGoodsHandler implements IOrderHandler<Order> {
    @Override
    public String handleOrder(Order order, IOrderHandleChain<Order> orderhandler) {
        String success=null;
        if(order.getStatus().equals("10")){
            if("1".equals(order.getRiderState())){
                success="订单已接";
            }else{
                order.setRiderState("1");
                success="接单成功";
            }
        }
        orderhandler.handleOrder(order);
        return success;
    }
}
