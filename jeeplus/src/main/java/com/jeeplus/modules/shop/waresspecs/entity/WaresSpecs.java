/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.waresspecs.entity;

import com.jeeplus.modules.shop.wares.entity.Wares;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商品规格Entity
 * @author lhh
 * @version 2019-12-02
 */
public class WaresSpecs extends DataEntity<WaresSpecs> {
	
	private static final long serialVersionUID = 1L;
	private Wares wares;		// 商品名称
	private String waresSpecs;		// 规格集合
	private String indexes;		// 下标以_分隔
	private String waresStock;		// 库存
	private String waresPrice;		// 价格
	
	public WaresSpecs() {
		super();
	}

	public WaresSpecs(String id){
		super(id);
	}

	@ExcelField(title="商品名称", align=2, sort=5)
	public Wares getWares() {
		return wares;
	}

	public void setWares(Wares wares) {
		this.wares = wares;
	}
	
	@ExcelField(title="规格集合", align=2, sort=6)
	public String getWaresSpecs() {
		return waresSpecs;
	}

	public void setWaresSpecs(String waresSpecs) {
		this.waresSpecs = waresSpecs;
	}
	
	@ExcelField(title="下标以_分隔", align=2, sort=7)
	public String getIndexes() {
		return indexes;
	}

	public void setIndexes(String indexes) {
		this.indexes = indexes;
	}
	
	@ExcelField(title="库存", align=2, sort=8)
	public String getWaresStock() {
		return waresStock;
	}

	public void setWaresStock(String waresStock) {
		this.waresStock = waresStock;
	}
	
	@ExcelField(title="价格", align=2, sort=9)
	public String getWaresPrice() {
		return waresPrice;
	}

	public void setWaresPrice(String waresPrice) {
		this.waresPrice = waresPrice;
	}
	
}