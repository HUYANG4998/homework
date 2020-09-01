package com.jeeplus.modules.shop.util.orderchange.filter;

import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;

/**
 * 骑手取完货之后跳配送中状态
 */
public class DistributionHandler implements IOrderHandler<Order> {
    @Override
    public String handleOrder(Order order, IOrderHandleChain<Order> orderhandler) {
        String success=null;
        if(order.getStatus().equals("5")){
            if("2".equals(order.getRiderState())){
                success="取货成功";
            }else{
                order.setRiderState("2");
                success="取货成功";
            }

        }
        orderhandler.handleOrder(order);
        return success;
    }
}
