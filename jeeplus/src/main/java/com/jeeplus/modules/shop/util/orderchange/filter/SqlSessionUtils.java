package com.jeeplus.modules.shop.util.orderchange.filter;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.service.OrderService;


public class SqlSessionUtils {
    private static OrderService orderService = SpringContextHolder.getBean(OrderService.class);
    public static void  saveOrder(Order order){
        orderService.save(order);
    }
}
