/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customerfollow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.customerfollow.entity.CustomerFollow;
import com.jeeplus.modules.shop.customerfollow.mapper.CustomerFollowMapper;

/**
 * 用户关注店铺管理Service
 * @author lhh
 * @version 2019-12-23
 */
@Service
@Transactional(readOnly = true)
public class CustomerFollowService extends CrudService<CustomerFollowMapper, CustomerFollow> {

	public CustomerFollow get(String id) {
		return super.get(id);
	}
	
	public List<CustomerFollow> findList(CustomerFollow customerFollow) {
		return super.findList(customerFollow);
	}
	
	public Page<CustomerFollow> findPage(Page<CustomerFollow> page, CustomerFollow customerFollow) {
		return super.findPage(page, customerFollow);
	}
	
	@Transactional(readOnly = false)
	public void save(CustomerFollow customerFollow) {
		super.save(customerFollow);
	}
	
	@Transactional(readOnly = false)
	public void delete(CustomerFollow customerFollow) {
		super.delete(customerFollow);
	}
	
}