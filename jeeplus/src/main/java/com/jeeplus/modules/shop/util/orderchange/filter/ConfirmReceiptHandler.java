package com.jeeplus.modules.shop.util.orderchange.filter;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.shop.api.earn.entity.Earn;
import com.jeeplus.modules.shop.api.earn.service.EarnService;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.entity.OrderDetail;
import com.jeeplus.modules.shop.order.service.OrderService;
import com.jeeplus.modules.shop.otherprice.entity.OtherPrice;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.systemtype.entity.SystemType;
import com.jeeplus.modules.shop.systemtype.service.SystemTypeService;
import com.jeeplus.modules.shop.util.SqlService;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.service.WaresService;

/**
 * 确认收货
 */
public class ConfirmReceiptHandler implements IOrderHandler<Order> {

    @Override
    public String handleOrder(Order order, IOrderHandleChain<Order> orderhandler) {
        String success=null;
        if(order.getStatus().equals("4")){
            if("2".equals(order.getOrderState())){
                success="确认收货已完成";
            }else{
                order.setOrderState("2");
                Store store = SqlService.storeService.get(order.getStore());
                Order orders=SqlService.orderService.get(order.getId());
                Double price=0.0;
                for (OrderDetail orderDetail:orders.getOrderDetailList()){
                    Double totalPrice =Double.valueOf(orderDetail.getTotalPrice());
                    SystemType systemType = SqlService.systemTypeService.get(SqlService.waresService.get(orderDetail.getWares()).getByOne());
                    if(systemType!=null){
                        if(!StringUtils.isEmpty(systemType.getPrice())){
                            price+=totalPrice-(totalPrice * (Double.valueOf(systemType.getPrice()) / 100));
                        }else{
                            price+=totalPrice;
                        }
                    }
                }

                if("1".equals(order.getPeiType())){
                    //物流
                    OtherPrice otherPrice = SqlService.otherPriceService.get("a7b6eb1e7e6e4580ac6c80cc9a3ee1be");
                    price+=Double.valueOf(otherPrice.getPrice());
                }
                store.setMoney(String.valueOf(Double.valueOf(store.getMoney())+price));
                Earn earn=new Earn("到账",String.valueOf(price),store.getId(),"1", DateUtil.now());
                SqlService.earnService.insertEarn(earn);
                SqlService.storeService.save(store);
                success="确认收货完成";
            }
        }
        orderhandler.handleOrder(order);
        return success;
    }
}
