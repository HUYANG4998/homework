/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customerassit.entity;

import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户点赞管理Entity
 * @author lhh
 * @version 2019-12-23
 */
public class CustomerAssit extends DataEntity<CustomerAssit> {
	
	private static final long serialVersionUID = 1L;
	private Customer customer;		// 用户名称
	private StoreMovie movie;		// 商家动态标题
	
	public CustomerAssit() {
		super();
	}

	public CustomerAssit(String id){
		super(id);
	}

	@ExcelField(title="用户名称", fieldType=Customer.class, value="customer.name", align=2, sort=1)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ExcelField(title="商家动态标题", fieldType=StoreMovie.class, value="movie.title", align=2, sort=2)
	public StoreMovie getMovie() {
		return movie;
	}

	public void setMovie(StoreMovie movie) {
		this.movie = movie;
	}
	
}