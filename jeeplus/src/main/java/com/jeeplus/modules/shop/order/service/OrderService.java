/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.mapper.OrderMapper;
import com.jeeplus.modules.shop.order.entity.OrderDetail;
import com.jeeplus.modules.shop.order.mapper.OrderDetailMapper;

/**
 * 订单管理Service
 * @author lhh
 * @version 2020-01-16
 */
@Service
@Transactional(readOnly = true)
public class OrderService extends CrudService<OrderMapper, Order> {

	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private OrderMapper orderMapper;

	public Order get(String id) {
		Order order = super.get(id);
		if(order!=null){
			order.setOrderDetailList(orderDetailMapper.findList(new OrderDetail(order)));
		}
		return order;
	}

	public List<Order> findList(Order order) {
		return super.findList(order);
	}
	public List<Order> findLists(Order order) {
		return orderMapper.findList(order);
	}

	public Page<Order> findPage(Page<Order> page, Order order) {
		return super.findPage(page, order);
	}

	@Transactional(readOnly = false)
	public void save(Order order) {
		super.save(order);
		for (OrderDetail orderDetail : order.getOrderDetailList()){
			if (orderDetail.getId() == null){
				continue;
			}
			if (OrderDetail.DEL_FLAG_NORMAL.equals(orderDetail.getDelFlag())){
				if (StringUtils.isBlank(orderDetail.getId())){
					orderDetail.setOrder(order);
					orderDetail.preInsert();
					orderDetailMapper.insert(orderDetail);
				}else{
					orderDetail.preUpdate();
					orderDetailMapper.update(orderDetail);
				}
			}else{
				orderDetailMapper.delete(orderDetail);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(Order order) {
		super.delete(order);
		orderDetailMapper.delete(new OrderDetail(order));
	}

	@Transactional(readOnly = false)
	public List<Order> selectByNumber(String number){
		List<Order> order=new ArrayList<>();
		if(!StringUtils.isEmpty(number)){
			order=orderMapper.selectOrderByNumber(number);
		}
		return order;
	}

	public List<Order> findListOrder(String createDate,String storeId) {
		List<Order> orders=new ArrayList<>();
		if(!StringUtils.isEmpty(createDate)&&!StringUtils.isEmpty(storeId)){
			orders=orderMapper.findListOrder(createDate,storeId);
		}
		return orders;
	}


	public Page<Order> findPageList(Page page, Order order) {
		dataRuleFilter(order);
		order.setPage(page);
		page.setList(orderMapper.findPageList(order));
		return page;
	}
}
