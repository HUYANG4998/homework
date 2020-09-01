/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customerfollow.entity;

import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.store.entity.Store;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户关注店铺管理Entity
 * @author lhh
 * @version 2019-12-23
 */
public class CustomerFollow extends DataEntity<CustomerFollow> {
	
	private static final long serialVersionUID = 1L;
	private Customer customer;		// 用户名称
	private Store store;		// 店铺名称
	
	public CustomerFollow() {
		super();
	}

	public CustomerFollow(String id){
		super(id);
	}

	@ExcelField(title="用户名称", fieldType=Customer.class, value="customer.name", align=2, sort=1)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ExcelField(title="店铺名称", fieldType=Store.class, value="store.name", align=2, sort=2)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
}