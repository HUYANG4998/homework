/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.riderrefuse.entity;

import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.order.entity.Order;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 骑手拒绝订单记录Entity
 * @author lhh
 * @version 2020-01-17
 */
public class RiderRefuse extends DataEntity<RiderRefuse> {
	
	private static final long serialVersionUID = 1L;
	private Rider rider;		// 骑手名称
	private Order order;		// 订单单号
	
	public RiderRefuse() {
		super();
	}

	public RiderRefuse(String id){
		super(id);
	}

	@ExcelField(title="骑手名称", fieldType=Rider.class, value="rider.name", align=2, sort=5)
	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
		this.rider = rider;
	}
	
	@ExcelField(title="订单单号", fieldType=Order.class, value="order.numbers", align=2, sort=6)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
}