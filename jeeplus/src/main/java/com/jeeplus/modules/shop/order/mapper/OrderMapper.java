/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.order.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.order.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单管理MAPPER接口
 * @author lhh
 * @version 2020-01-16
 */
@MyBatisMapper
public interface OrderMapper extends BaseMapper<Order> {
	List<Order> selectOrderByNumber(String number);

    List<Order> findListOrder(@Param("createDate") String  createDate,@Param("storeId")String storeId);

    List findPageList(Order order);
}