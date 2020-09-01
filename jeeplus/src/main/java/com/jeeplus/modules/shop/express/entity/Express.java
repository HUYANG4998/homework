/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.express.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 快递方式配置Entity
 * @author lhh
 * @version 2020-01-15
 */
public class Express extends DataEntity<Express> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 快递名称
	
	public Express() {
		super();
	}

	public Express(String id){
		super(id);
	}

	@ExcelField(title="快递名称", align=2, sort=5)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}