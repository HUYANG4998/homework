/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.ordertotal.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单统计Entity
 * @author lhh
 * @version 2020-01-10
 */
public class OrderTotal extends DataEntity<OrderTotal> {
	
	private static final long serialVersionUID = 1L;
	private String orderIds;		// 订单id集合
	private String totalPrice;		// 总金额
	
	public OrderTotal() {
		super();
	}

	public OrderTotal(String id){
		super(id);
	}

	@ExcelField(title="订单id集合", align=2, sort=7)
	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}
	
	@ExcelField(title="总金额", align=2, sort=8)
	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}