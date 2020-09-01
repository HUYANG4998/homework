package com.jeeplus.modules.shop.util.orderchange.filter;

import com.alipay.api.AlipayApiException;
import com.jeeplus.modules.shop.api.payutils.alipay.AlipayTranfer;
import com.jeeplus.modules.shop.api.payutils.wxpay.WXpayController;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandleChain;
import com.jeeplus.modules.shop.util.orderchange.IOrderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 退款成功
 */
@Component
public class RefundSuccessHandler implements IOrderHandler<Order> {


    @Override
    public String handleOrder(Order order, IOrderHandleChain<Order> orderhandler) {
        String success=null;
        if("2".equals(order.getStatus())){
            //退钱操作
            try {
                if("1".equals(order.getAudit())){
                    success="退款已成功,请不要重复退款";
                }else{
                    String payType = order.getPayType();
                    boolean isSuccess=false;
                    String number=order.getId();

                    if("0".equals(payType)){
                        isSuccess = AlipayTranfer.AlipayRefundMoney(Double.valueOf(order.getByOne()), order.getNumbers());

                    }else if("1".equals(payType)){
                        isSuccess = WXpayController.wxRefund(Double.valueOf(order.getByOne()), order.getNumbers());

                    }
                    if(isSuccess){
                        order.setAudit("1");
                        success="退款成功";
                    }else{
                        success="退款失败";
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        orderhandler.handleOrder(order);
        return success;
    }

}
