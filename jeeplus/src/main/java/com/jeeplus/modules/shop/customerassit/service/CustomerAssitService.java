/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customerassit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.customerassit.entity.CustomerAssit;
import com.jeeplus.modules.shop.customerassit.mapper.CustomerAssitMapper;

/**
 * 用户点赞管理Service
 * @author lhh
 * @version 2019-12-23
 */
@Service
@Transactional(readOnly = true)
public class CustomerAssitService extends CrudService<CustomerAssitMapper, CustomerAssit> {

	public CustomerAssit get(String id) {
		return super.get(id);
	}
	
	public List<CustomerAssit> findList(CustomerAssit customerAssit) {
		return super.findList(customerAssit);
	}
	
	public Page<CustomerAssit> findPage(Page<CustomerAssit> page, CustomerAssit customerAssit) {
		return super.findPage(page, customerAssit);
	}
	
	@Transactional(readOnly = false)
	public void save(CustomerAssit customerAssit) {
		super.save(customerAssit);
	}
	
	@Transactional(readOnly = false)
	public void delete(CustomerAssit customerAssit) {
		super.delete(customerAssit);
	}
	
}