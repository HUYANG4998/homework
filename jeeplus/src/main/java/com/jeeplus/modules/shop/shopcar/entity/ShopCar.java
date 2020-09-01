/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.shopcar.entity;

import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;
import com.jeeplus.modules.shop.wares.entity.Wares;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import javax.persistence.Transient;
import java.util.Map;

/**
 * 购物车管理Entity
 * @author lhh
 * @version 2019-12-27
 */
public class ShopCar extends DataEntity<ShopCar> {

	private static final long serialVersionUID = 1L;
	private Customer customer;		// 用户名称
	private Store store;		// 商家名称
	private Wares wares;		// 商品id
	private String name;		// 商品名称
	private String price;		// 总价格
	private String waresPrice;		// 商品单价
	private String num;		// 数量
	private String title;		// 商品标题
	private String guige;		// 规格
	private String storeId;		// 商家id
	private String waresImg;    //商品主图1个
	private Integer stock;  //前端显示规格库存
	private String presentPrice;//现价

	public ShopCar() {
		super();
	}

	public ShopCar(String id){
		super(id);
	}

	@ExcelField(title="用户名称", fieldType=Customer.class, value="customer.name", align=2, sort=7)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ExcelField(title="商家名称", fieldType=Store.class, value="store.name", align=2, sort=8)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@ExcelField(title="商品id", fieldType=Wares.class, value="wares.name", align=2, sort=9)
	public Wares getWares() {
		return wares;
	}

	public void setWares(Wares wares) {
		this.wares = wares;
	}

	@ExcelField(title="商品名称", align=2, sort=10)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="总价格", align=2, sort=11)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@ExcelField(title="商品单价", align=2, sort=12)
	public String getWaresPrice() {
		return waresPrice;
	}

	public void setWaresPrice(String waresPrice) {
		this.waresPrice = waresPrice;
	}

	@ExcelField(title="数量", align=2, sort=13)
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@ExcelField(title="商品标题", align=2, sort=14)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ExcelField(title="规格", align=2, sort=15)
	public String getGuige() {
		return guige;
	}

	public void setGuige(String guige) {
		this.guige = guige;
	}

	@Transient
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId=storeId;
	}

	@Transient
	public String getWaresImg() {
		return waresImg;
	}

	public void setWaresImg(String waresImg) {
		this.waresImg=waresImg;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getPresentPrice() {
		return presentPrice;
	}

	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}
}
