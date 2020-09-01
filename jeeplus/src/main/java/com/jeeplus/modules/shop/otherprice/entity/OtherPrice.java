/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.otherprice.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 配送费设置Entity
 * @author lhh
 * @version 2020-01-03
 */
public class OtherPrice extends DataEntity<OtherPrice> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String distance;		// 公里数
	private String price;		// 价格
	private String state;		// 是否开启
	
	public OtherPrice() {
		super();
	}

	public OtherPrice(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=4)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="公里数", align=2, sort=5)
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	@ExcelField(title="价格", align=2, sort=6)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	@ExcelField(title="是否开启", dictType="yes_no", align=2, sort=7)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}