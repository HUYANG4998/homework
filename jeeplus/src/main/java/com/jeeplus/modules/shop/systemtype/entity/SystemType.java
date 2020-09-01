/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemtype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.shop.wares.entity.Wares;

import javax.persistence.Transient;
import java.util.List;

/**
 * 平台分设置Entity
 * @author lhh
 * @version 2020-01-16
 */
public class SystemType extends DataEntity<SystemType> {

	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String sort;		// 排序
	private String price;		// 费率设置（%）
	private List<Wares> waresList;//商品集合

	public SystemType() {
		super();
	}

	public SystemType(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="排序", align=2, sort=2)
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@ExcelField(title="费率设置（%）", align=2, sort=9)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Transient
	public List<Wares> getWaresList() {
		return waresList;
	}

	public void setWaresList(List<Wares> waresList) {
		this.waresList=waresList;
	}
}
