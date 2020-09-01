/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.warestype.entity;

import com.jeeplus.modules.shop.store.entity.Store;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.shop.wares.entity.Wares;

import javax.persistence.Transient;
import java.util.List;

/**
 * 商品分类Entity
 * @author lhh
 * @version 2019-12-02
 */
public class WaresType extends DataEntity<WaresType> {

	private static final long serialVersionUID = 1L;
	private Store store;		// 商家名称
	private String name;		// 分类名称
	private String byOne;		// 备用字段1
	private String byTwo;		// 备用字段2
	private List<Wares>  list;//商品集合

	public WaresType() {
		super();
	}

	public WaresType(String id){
		super(id);
	}

	@ExcelField(title="商家名称", fieldType=Store.class, value="store.name", align=2, sort=4)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@ExcelField(title="分类名称", align=2, sort=5)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="备用字段1", align=2, sort=6)
	public String getByOne() {
		return byOne;
	}

	public void setByOne(String byOne) {
		this.byOne = byOne;
	}

	@ExcelField(title="备用字段2", align=2, sort=7)
	public String getByTwo() {
		return byTwo;
	}

	public void setByTwo(String byTwo) {
		this.byTwo = byTwo;
	}

	@Transient
	public List<Wares> getList() {
		return list;
	}

	public void setList(List<Wares> list) {
		this.list=list;
	}
}
