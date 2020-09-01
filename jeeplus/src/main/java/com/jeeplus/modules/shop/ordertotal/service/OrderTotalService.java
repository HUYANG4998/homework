/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.ordertotal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.ordertotal.entity.OrderTotal;
import com.jeeplus.modules.shop.ordertotal.mapper.OrderTotalMapper;

/**
 * 订单统计Service
 * @author lhh
 * @version 2020-01-10
 */
@Service
@Transactional(readOnly = true)
public class OrderTotalService extends CrudService<OrderTotalMapper, OrderTotal> {

	public OrderTotal get(String id) {
		return super.get(id);
	}
	
	public List<OrderTotal> findList(OrderTotal orderTotal) {
		return super.findList(orderTotal);
	}
	
	public Page<OrderTotal> findPage(Page<OrderTotal> page, OrderTotal orderTotal) {
		return super.findPage(page, orderTotal);
	}
	
	@Transactional(readOnly = false)
	public void save(OrderTotal orderTotal) {
		super.save(orderTotal);
	}
	
	@Transactional(readOnly = false)
	public void delete(OrderTotal orderTotal) {
		super.delete(orderTotal);
	}
	
}