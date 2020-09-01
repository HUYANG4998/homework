package com.jeeplus.modules.shop.util.orderchange;

import com.jeeplus.modules.shop.util.orderchange.filter.*;

import java.util.ArrayList;
import java.util.List;

public class OrderHandlerContainer {

    private OrderHandlerContainer(){}

    public static List<IOrderHandler> getHandlerList(){
        List<IOrderHandler> list=new ArrayList<>();
        list.add(new DeliveryHandler());
        list.add(new RefundHandler());
        list.add(new ConfirmReceiptHandler());
        list.add(new RefundFailHandler());
        list.add(new RefundSuccessHandler());
        list.add(new PickGoodsHandler());
        list.add(new CancelOrder());
        list.add(new DistributionHandler());
        list.add(new ArriveHandler());

        return list;
    }
}
