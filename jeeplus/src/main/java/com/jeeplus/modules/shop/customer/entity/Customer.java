/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customer.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户管理Entity
 * @author lhh
 * @version 2019-11-21
 */
public class Customer extends DataEntity<Customer> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 用户名称
	private String phone;		// 联系方式
	private String pwd;		// 密码
	private String img;		// 头像
	private String weiId;		// 微信openid
	private String qqId;		// qqopenid
	private String byOne;		// 备用字段1
	private String byTwo;		// 备用字段2
	private String byThree;		// 备用字段3
	private String isUser;
	
	public Customer() {
		super();
	}

	public Customer(String id){
		super(id);
	}

	@ExcelField(title="用户名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="联系方式", align=2, sort=2)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="密码", align=2, sort=3)
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	@ExcelField(title="头像", align=2, sort=4)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	@ExcelField(title="微信openid", align=2, sort=5)
	public String getWeiId() {
		return weiId;
	}

	public void setWeiId(String weiId) {
		this.weiId = weiId;
	}
	
	@ExcelField(title="qqopenid", align=2, sort=6)
	public String getQqId() {
		return qqId;
	}

	public void setQqId(String qqId) {
		this.qqId = qqId;
	}
	
	@ExcelField(title="备用字段1", align=2, sort=13)
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
	
	@ExcelField(title="备用字段3", align=2, sort=15)
	public String getByThree() {
		return byThree;
	}

	public void setByThree(String byThree) {
		this.byThree = byThree;
	}

	public String getIsUser() {
		return isUser;
	}

	public void setIsUser(String isUser) {
		this.isUser = isUser;
	}
}