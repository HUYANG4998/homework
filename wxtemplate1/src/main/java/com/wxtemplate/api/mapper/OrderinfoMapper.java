package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Orderinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单详情 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface OrderinfoMapper extends BaseMapper<Orderinfo> {


    List<Orderinfo> selectOrderInfoByOrderIdAndStatus(String orderid,String status);

    void deleteByOrderId(String orderid);

    List<Orderinfo> selectOrderinfoByUseridAndOrderidAndType(Map<String, Object> map);

    Map<String, Object> selectOrderByOrderinfoId(String orderinfoid);

    List<Map<String, Object>> selectReserveOrderinfoByOrderidAndType(String orderid, String type);

    Integer selectOrderInfoByOrderId(String orderid);

    List<Orderinfo> selectOrderinfoByOrderid(String orderid);

    List<Orderinfo> selectInfoByOrderid(String orderid);

    List<Map<String, Object>> selectCompleteOrderinfoByOrderid(String orderid,String type);

    Map<String, Object> selectOrderinfoByOrderinfoid(String orderinfoid);

    List<Orderinfo> selectOrderinfoByUseridAndOrderid(String userid, String orderid);

    List<Orderinfo> selectOrderinfoCompleteByOrderid(String orderid);

    List<String> selectOrderinfoUserIdByOrderId(String orderid);

    List<Orderinfo> selectOrderinfoCompleteByUseridAndOrderid(String userid, String orderid);

    List<Orderinfo> selectOrderinfoList();

    List<Orderinfo> selectOrderinfoOneDay();

    List<Map<String,Object>> selectOrderinfoByOrderidAndType(String orderid, String type);

    Integer selectOrderinfoByCaridAndColor(@Param("orderid") String orderid, @Param("carid")String carid, @Param("carcolor")String carcolor,@Param("infocartype")String infocartype);

    List<Orderinfo> selectOrderinfoLate();

    List<Orderinfo> selectOrderinfoSwitch();

    List<Orderinfo> selectOrderinfoSettlement();

    void updateOrderinfoByOrderid(String orderid);

    List<Map<String,Object>> selectOrderinfoByOrderidAndtypeAndCartype(Map<String, Object> result);

    Integer selectOrderinfoByResNumber(String orderid,String cartype);

    List<Map<String, Object>> payorderinfo(Orderinfo orderinfo);

    Integer selectUnfinished(String orderid);

    List<Orderinfo> selectOngoing(String orderid);

    List<Map<String, Object>> reserveCarOrders(Map<String, Object> map);

    void deleteByOrderIdAndType(String orderid);

    Integer selectReceived(String status, String type,String userid);

    Integer selectReservation(String status, String type,String userid);

    Integer selectOrderInfoByOrderIdAndCarId(@Param("orderId") String orderId, @Param("carId") String carId);
}
