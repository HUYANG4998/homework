package com.jeeplus.modules.shop.util.orderchange;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.util.orderchange.filter.OrderSave;
import com.jeeplus.modules.shop.util.orderchange.filter.SqlSessionUtils;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class OrderHandleChain<T> implements IOrderHandleChain<T> {

    private int index = 0;

    private String success = null;

    private static List<IOrderHandler> list;

    static {
        list = OrderHandlerContainer.getHandlerList();
        /*list.add(new OrderSave());*/
    }

    @Override
    public String handleOrder(T order) {
        if (list.size() > 0 && index != list.size()) {
            IOrderHandler iOrderHandler = list.get(index++);
            String isSuccess = iOrderHandler.handleOrder(order, this);
            if (!StringUtils.isEmpty(isSuccess)) {
                success = isSuccess;
            }
        }
        return success;
    }

    @Override
    public void orderSave(T order) {
        SqlSessionUtils.saveOrder((Order)order);
    }
}
