/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storemoviereply.entity;

import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.store.entity.Store;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商家动态回复Entity
 * @author lhh
 * @version 2020-01-07
 */
public class StoreMovieReply extends DataEntity<StoreMovieReply> {
	
	private static final long serialVersionUID = 1L;
	private StoreMovie movie;		// 动态标题
	private Customer customer;		// 用户名称
	private String lastId;		// 上一级回复的id
	private Store store;		// 商家名称
	private String yesNo;		// 是否商家
	
	public StoreMovieReply() {
		super();
	}

	public StoreMovieReply(String id){
		super(id);
	}

	@ExcelField(title="动态标题", fieldType=StoreMovie.class, value="movie.title", align=2, sort=1)
	public StoreMovie getMovie() {
		return movie;
	}

	public void setMovie(StoreMovie movie) {
		this.movie = movie;
	}
	
	@ExcelField(title="用户名称", fieldType=Customer.class, value="customer.name", align=2, sort=2)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ExcelField(title="上一级回复的id", align=2, sort=3)
	public String getLastId() {
		return lastId;
	}

	public void setLastId(String lastId) {
		this.lastId = lastId;
	}
	
	@ExcelField(title="商家名称", fieldType=Store.class, value="store.name", align=2, sort=8)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
	@ExcelField(title="是否商家", align=2, sort=11)
	public String getYesNo() {
		return yesNo;
	}

	public void setYesNo(String yesNo) {
		this.yesNo = yesNo;
	}
	
}