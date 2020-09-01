/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.order.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.order.entity.OrderDetail;

/**
 * 订单明细MAPPER接口
 * @author lhh
 * @version 2020-01-16
 */
@MyBatisMapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
	
}