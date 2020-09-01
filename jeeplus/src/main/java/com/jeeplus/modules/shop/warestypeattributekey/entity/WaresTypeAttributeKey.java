/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.warestypeattributekey.entity;

import com.jeeplus.modules.shop.warestype.entity.WaresType;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.shop.warestypeattributevalue.entity.WaresTypeAttributeValue;

import javax.persistence.Transient;
import java.util.List;

/**
 * 分类属性Entity
 * @author lhh
 * @version 2019-12-24
 */
public class WaresTypeAttributeKey extends DataEntity<WaresTypeAttributeKey> {

	private static final long serialVersionUID = 1L;
	private WaresType waresType;		// 所属分类
	private String name;		// 名称
	private String sort;		// 排序方式
	private String isImg;
	private List<WaresTypeAttributeValue> waresTypeAttributeValueList;

	public WaresTypeAttributeKey() {
		super();
	}

	public WaresTypeAttributeKey(String id){
		super(id);
	}

	@ExcelField(title="所属分类", fieldType=WaresType.class, value="waresType.name", align=2, sort=5)
	public WaresType getWaresType() {
		return waresType;
	}

	public void setWaresType(WaresType waresType) {
		this.waresType = waresType;
	}

	@ExcelField(title="名称", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="排序方式", align=2, sort=7)
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Transient
	public List<WaresTypeAttributeValue> getWaresTypeAttributeValueList() {
		return waresTypeAttributeValueList;
	}

	public void setWaresTypeAttributeValueList(List<WaresTypeAttributeValue> waresTypeAttributeValueList) {
		this.waresTypeAttributeValueList=waresTypeAttributeValueList;
	}

	public String getIsImg() {
		return isImg;
	}

	public void setIsImg(String isImg) {
		this.isImg = isImg;
	}
}
