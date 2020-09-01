/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storediscount.entity;

import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.wares.entity.Wares;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商家优惠Entity
 * @author lhh
 * @version 2019-11-29
 */
public class StoreDiscount extends DataEntity<StoreDiscount> {
	
	private static final long serialVersionUID = 1L;
	private Store store;		// 商家名称
	private String manMoney;		// 满多少金额
	private String msg;		// 减/送/折扣
	private String yesNo;		// 是否开启（0：停止1：开启）
	private String discountState;		// 优惠类型（满减/满送/折扣）
	private String discountType;		// 优惠所属（商家/商品）
	private Wares wares;		// 商品名称
	private Date startDate;		// 开启时间
	private Date endDate;		// 结束时间
	
	public StoreDiscount() {
		super();
	}

	public StoreDiscount(String id){
		super(id);
	}

	@ExcelField(title="商家名称", fieldType=Store.class, value="store.name", align=2, sort=1)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
	@ExcelField(title="满多少金额", align=2, sort=2)
	public String getManMoney() {
		return manMoney;
	}

	public void setManMoney(String manMoney) {
		this.manMoney = manMoney;
	}
	
	@ExcelField(title="减/送/折扣", align=2, sort=3)
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@ExcelField(title="是否开启（0：停止1：开启）", dictType="yes_no", align=2, sort=4)
	public String getYesNo() {
		return yesNo;
	}

	public void setYesNo(String yesNo) {
		this.yesNo = yesNo;
	}
	
	@ExcelField(title="优惠类型（满减/满送/折扣）", dictType="discount_state", align=2, sort=5)
	public String getDiscountState() {
		return discountState;
	}

	public void setDiscountState(String discountState) {
		this.discountState = discountState;
	}
	
	@ExcelField(title="优惠所属（商家/商品）", dictType="discount_type", align=2, sort=6)
	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	
	@ExcelField(title="商品名称", fieldType=Wares.class, value="wares.name", align=2, sort=7)
	public Wares getWares() {
		return wares;
	}

	public void setWares(Wares wares) {
		this.wares = wares;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开启时间", align=2, sort=8)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=9)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}