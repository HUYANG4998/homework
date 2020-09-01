/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customeraddress.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.customeraddress.entity.CustomerAddress;
import com.jeeplus.modules.shop.customeraddress.mapper.CustomerAddressMapper;

/**
 * 用户收货地址管理Service
 * @author lhh
 * @version 2019-12-23
 */
@Service
@Transactional(readOnly = true)
public class CustomerAddressService extends CrudService<CustomerAddressMapper, CustomerAddress> {

	public CustomerAddress get(String id) {
		return super.get(id);
	}
	
	public List<CustomerAddress> findList(CustomerAddress customerAddress) {
		return super.findList(customerAddress);
	}
	
	public Page<CustomerAddress> findPage(Page<CustomerAddress> page, CustomerAddress customerAddress) {
		return super.findPage(page, customerAddress);
	}
	
	@Transactional(readOnly = false)
	public void save(CustomerAddress customerAddress) {
		super.save(customerAddress);
	}
	
	@Transactional(readOnly = false)
	public void delete(CustomerAddress customerAddress) {
		super.delete(customerAddress);
	}
	
}