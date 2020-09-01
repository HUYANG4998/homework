package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-17
 */
public interface IOrdersService extends IService<Orders> {

    List<Orders> takeOrdersHall(String userid);

    List<Map<String,Object>> selectAllOrder();

    Map<String, Object> selectOrderByOrderid(String orderid,String type);

    void orderAudit(Map<String, Object> result);

    List<Map<String,Object>> selectOrderSingle(String type,String ordernumber);

    void orderSingleAudit(String orderid, String remark);

    void updateOrdersRemark(Orders order);

    List<Map<String,Object>> selectOrderComplete(String orderstatus,String ordernumber);

    Map<String, Object> selectOrderNumber(String userid);

    void ordercancel(String orderid);

    String orderDeleteOrBank(String orderid);
}
