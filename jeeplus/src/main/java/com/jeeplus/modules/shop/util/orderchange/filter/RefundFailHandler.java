package com.jeeplus.modules.shop.util.orderchange.filter;

import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;

/**
 * 拒绝退款
 */
public class RefundFailHandler implements IOrderHandler<Order> {
    @Override
    public String handleOrder(Order order, IOrderHandleChain<Order> orderhandler) {
        String success=null;
        if(order.getStatus().equals("3")){
            if("2".equals(order.getAudit())){
                success="订单退款已拒绝";
            }else{
               /* order.setOrderState("1");
                order.setStoreState("1");*/
                order.setAudit("2");
                success="拒绝退款成功";
            }

        }
        orderhandler.handleOrder(order);
        return success;
    }
}
