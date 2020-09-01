/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customerasses.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.customerasses.entity.CustomerAsses;
import com.jeeplus.modules.shop.customerasses.mapper.CustomerAssesMapper;

/**
 * 商品评论Service
 * @author lhh
 * @version 2020-01-06
 */
@Service
@Transactional(readOnly = true)
public class CustomerAssesService extends CrudService<CustomerAssesMapper, CustomerAsses> {

	public CustomerAsses get(String id) {
		return super.get(id);
	}
	
	public List<CustomerAsses> findList(CustomerAsses customerAsses) {
		return super.findList(customerAsses);
	}
	
	public Page<CustomerAsses> findPage(Page<CustomerAsses> page, CustomerAsses customerAsses) {
		return super.findPage(page, customerAsses);
	}
	
	@Transactional(readOnly = false)
	public void save(CustomerAsses customerAsses) {
		super.save(customerAsses);
	}
	
	@Transactional(readOnly = false)
	public void delete(CustomerAsses customerAsses) {
		super.delete(customerAsses);
	}
	
}