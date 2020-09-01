package com.jeeplus.modules.shop.util.orderchange.filter;

import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;

/**
 * 发起退款
 */
public class RefundHandler implements IOrderHandler<Order> {

    @Override
    public String handleOrder(Order order, IOrderHandleChain<Order> orderhandler) {
        String success=null;
        if(order.getStatus().equals("1")){
            if("0".equals(order.getAudit())){
                success="订单已发起退款";
            }else{

                order.setStoreState("2");
                order.setAudit("0");
                success="发起退款";
            }

        }
        orderhandler.handleOrder(order);
        return success;
    }
}
