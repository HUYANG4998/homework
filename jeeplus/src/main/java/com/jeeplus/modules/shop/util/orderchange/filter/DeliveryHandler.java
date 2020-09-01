package com.jeeplus.modules.shop.util.orderchange.filter;

import cn.hutool.core.date.DateUtil;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 发货
 */
public class DeliveryHandler implements IOrderHandler<Order> {

    @Override
    public String handleOrder(Order order, IOrderHandleChain orderhandler) {
        String success=null;
        if ("0".equals(order.getStatus())) {
            if("1".equals(order.getStoreState())){
                success="发货已成功";
            }else{
                order.setStoreState("1");
                order.setDeliverytime(DateUtil.now());
                success="发货成功";
            }

        }
        orderhandler.handleOrder(order);
        return success;

    }
}
