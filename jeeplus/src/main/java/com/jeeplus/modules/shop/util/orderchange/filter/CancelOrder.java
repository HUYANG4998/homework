package com.jeeplus.modules.shop.util.orderchange.filter;

import cn.hutool.core.date.DateUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.entity.OrderDetail;
import com.jeeplus.modules.shop.util.SqlService;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.waresspecs.entity.WaresSpecs;

/**
 * 订单取消
 */
public class CancelOrder implements IOrderHandler<Order> {
    @Override
    public String handleOrder(Order order, IOrderHandleChain<Order> orderhandler) {
        String success=null;
        if(order.getStatus().equals("7")){
            if("3".equals(order.getOrderState())){
                success="订单已取消";
            }else{
                order.setOrderState("3");
                Order orders=SqlService.orderService.get(order.getId());
                for (OrderDetail orderDetail:orders.getOrderDetailList()){
                    String num=orderDetail.getNum();
                    String spec=orderDetail.getByTwo();
                    if(!StringUtils.isEmpty(spec)){
                        WaresSpecs waresSpecs= SqlService.waresSpecsService.get(spec);
                        waresSpecs.setWaresStock(String.valueOf(Integer.valueOf(waresSpecs.getWaresStock())+Integer.valueOf(num)));
                        SqlService.waresSpecsService.save(waresSpecs);
                    }else{
                        Wares w=SqlService.waresService.get(orderDetail.getWares().getId());
                        w.setStock(String.valueOf(Integer.valueOf(w.getStock())+Integer.valueOf(num)));
                        SqlService.waresService.save(w);
                    }
                }

                success="取消成功";
            }

        }
        orderhandler.handleOrder(order);
        return success;
    }
}
