/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.warestypeattributevalue.entity;

import com.jeeplus.modules.shop.warestypeattributekey.entity.WaresTypeAttributeKey;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 分类属性值Entity
 * @author lhh
 * @version 2019-12-02
 */
public class WaresTypeAttributeValue extends DataEntity<WaresTypeAttributeValue> {
	
	private static final long serialVersionUID = 1L;
	private WaresTypeAttributeKey attributeKey;		// 属性key
	private String attributeValue;		// 值
	private String sort;		// 排序
	private String img;
	public WaresTypeAttributeValue() {
		super();
	}

	public WaresTypeAttributeValue(String id){
		super(id);
	}

	@ExcelField(title="属性key", fieldType=WaresTypeAttributeKey.class, value="attributeKey.name", align=2, sort=5)
	public WaresTypeAttributeKey getAttributeKey() {
		return attributeKey;
	}

	public void setAttributeKey(WaresTypeAttributeKey attributeKey) {
		this.attributeKey = attributeKey;
	}
	
	@ExcelField(title="值", align=2, sort=6)
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	@ExcelField(title="排序", align=2, sort=7)
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}