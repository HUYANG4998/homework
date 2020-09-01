package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Orderinfo;
import com.wxtemplate.api.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-17
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    Orders selectNewOrderByUserId(String userid);

    Map<String, Object> selectMainOrderInfoByNum(int num,String orderid);
    Map<String,Object> selectDeputyOrderInfoByNum(String orderid);

    List<Orders> selectOrderByUserIdAndOrderstatus(String userid, String orderstatus);

    List<Map<String,Object>> selectOrderByOrderInfo(Map<String,Object> map);

    Map<String, Object> selectOrdersByOrderId(String orderid);

    List<Map<String, Object>> selectTakeMyOrderInfo(Map<String, Object> map);

    List<Orders> selectOrderList(String userid);

    List<Map<String, Object>> selectAllOrder();


    List<Map<String,Object>> selectOrdersByType(String type,String ordernumber);


    List<Map<String,Object>> selectOrderComplete(String orderstatus,String ordernumber);

    String selectByOrderNumber(String ordernumber);

    Integer selectByOrderstatus(String orderstatus,String userid);

    List<Orders> selectAfterMarry();
}
