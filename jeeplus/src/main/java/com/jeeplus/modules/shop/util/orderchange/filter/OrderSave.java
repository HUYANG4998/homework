package com.jeeplus.modules.shop.util.orderchange.filter;

import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;

public class OrderSave implements IOrderHandler<Order> {

    @Override
    public String handleOrder(Order order, IOrderHandleChain<Order> orderhandler) {
        SqlSessionUtils.saveOrder(order);
        return null;
    }
}
