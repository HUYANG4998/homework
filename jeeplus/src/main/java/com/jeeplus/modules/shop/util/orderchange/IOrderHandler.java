package com.jeeplus.modules.shop.util.orderchange;

import com.jeeplus.modules.shop.order.entity.Order;

public interface IOrderHandler<T> {
    String handleOrder(T order,IOrderHandleChain<T> orderhandler);
}
