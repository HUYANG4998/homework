/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.order.entity;

import com.jeeplus.modules.shop.wares.entity.Wares;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单明细Entity
 * @author lhh
 * @version 2020-01-16
 */
public class OrderDetail extends DataEntity<OrderDetail> {
	
	private static final long serialVersionUID = 1L;
	private Wares wares;		// 商品名称
	private String waresPrice;		// 商品单价
	private String num;		// 数量
	private String totalPrice;		// 总价
	private String numbers;		// 订单号
	private Order order;		// 主订单编号 父类
	private String byOne;		// 商品规格
	private String byTwo;		// 备用字段2
	private String byThree;		// 商品图片地址截取第一个
	
	public OrderDetail() {
		super();
	}

	public OrderDetail(String id){
		super(id);
	}

	public OrderDetail(Order order){
		this.order = order;
	}

	@ExcelField(title="商品名称", fieldType=Wares.class, value="wares.name", align=2, sort=6)
	public Wares getWares() {
		return wares;
	}

	public void setWares(Wares wares) {
		this.wares = wares;
	}
	
	@ExcelField(title="商品单价", align=2, sort=7)
	public String getWaresPrice() {
		return waresPrice;
	}

	public void setWaresPrice(String waresPrice) {
		this.waresPrice = waresPrice;
	}
	
	@ExcelField(title="数量", align=2, sort=8)
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	
	@ExcelField(title="总价", align=2, sort=9)
	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	@ExcelField(title="订单号", align=2, sort=10)
	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@ExcelField(title="商品规格", align=2, sort=13)
	public String getByOne() {
		return byOne;
	}

	public void setByOne(String byOne) {
		this.byOne = byOne;
	}
	
	@ExcelField(title="备用字段2", align=2, sort=14)
	public String getByTwo() {
		return byTwo;
	}

	public void setByTwo(String byTwo) {
		this.byTwo = byTwo;
	}
	
	@ExcelField(title="商品图片地址截取第一个", align=2, sort=15)
	public String getByThree() {
		return byThree;
	}

	public void setByThree(String byThree) {
		this.byThree = byThree;
	}
	
}