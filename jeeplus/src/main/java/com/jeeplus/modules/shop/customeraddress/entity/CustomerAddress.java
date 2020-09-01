/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customeraddress.entity;

import com.jeeplus.modules.shop.customer.entity.Customer;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户收货地址管理Entity
 * @author lhh
 * @version 2019-12-23
 */
public class CustomerAddress extends DataEntity<CustomerAddress> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 收货人姓名
	private Customer customer;		// 用户名称
	private String customerSex;		// 收货人性别
	private String phone;		// 电话号码
	private String address;		// 收货地址
	private String addressType;		// 地址类型
	private String addressDetail;		// 详细地址
	private String lng;		// 经度
	private String lat;		// 纬度
	private String yesNo;		// 是否默认
	
	public CustomerAddress() {
		super();
	}

	public CustomerAddress(String id){
		super(id);
	}

	@ExcelField(title="收货人姓名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="用户名称", fieldType=Customer.class, value="customer.name", align=2, sort=2)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ExcelField(title="收货人性别", dictType="customer_sex", align=2, sort=3)
	public String getCustomerSex() {
		return customerSex;
	}

	public void setCustomerSex(String customerSex) {
		this.customerSex = customerSex;
	}
	
	@ExcelField(title="电话号码", align=2, sort=4)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="收货地址", align=2, sort=5)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="地址类型", dictType="address_type", align=2, sort=6)
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	
	@ExcelField(title="详细地址", align=2, sort=7)
	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}
	
	@ExcelField(title="经度", align=2, sort=8)
	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}
	
	@ExcelField(title="纬度", align=2, sort=9)
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
	
	@ExcelField(title="是否默认", dictType="yes_no", align=2, sort=10)
	public String getYesNo() {
		return yesNo;
	}

	public void setYesNo(String yesNo) {
		this.yesNo = yesNo;
	}
	
}