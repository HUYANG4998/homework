package com.jeeplus.modules.shop.util.orderchange;

import com.jeeplus.modules.shop.order.entity.Order;

public class OrderFactory {
    public static void main(String[] args){
        Order order=new Order();
        order.setStatus("0");
        OrderHandleChain orderHandleChain=new OrderHandleChain();
        orderHandleChain.handleOrder(order);
    }
}
