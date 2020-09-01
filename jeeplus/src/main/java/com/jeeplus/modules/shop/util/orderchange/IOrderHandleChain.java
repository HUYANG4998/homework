package com.jeeplus.modules.shop.util.orderchange;

import com.jeeplus.modules.shop.order.entity.Order;

public interface IOrderHandleChain<T> {
    String handleOrder(T order);
    void orderSave(T order);
}
