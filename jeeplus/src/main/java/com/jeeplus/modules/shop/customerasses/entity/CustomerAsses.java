/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customerasses.entity;

import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.wares.entity.Wares;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商品评论Entity
 * @author lhh
 * @version 2020-01-06
 */
public class CustomerAsses extends DataEntity<CustomerAsses> {
	
	private static final long serialVersionUID = 1L;
	private Customer customer;		// 用户名称
	private Wares wares;		// 商品名称
	private String contents;		// 内容
	private String img;		// 图片
	private String yesNo;		// 是否匿名
	private String name;		// 名称
	private String orderId;		// 订单id
	private Integer star;		// 星级
	private String descCon;		// 描述相符
	private String serviceAtt;		// 服务态度
	private String pageSize;
	private String pageNum;

	public CustomerAsses() {
		super();
	}

	public CustomerAsses(String id){
		super(id);
	}

	@ExcelField(title="用户名称", fieldType=Customer.class, value="customer.name", align=2, sort=5)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ExcelField(title="商品名称", fieldType=Wares.class, value="wares.name", align=2, sort=6)
	public Wares getWares() {
		return wares;
	}

	public void setWares(Wares wares) {
		this.wares = wares;
	}
	
	@ExcelField(title="内容", align=2, sort=7)
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	@ExcelField(title="图片", align=2, sort=8)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	@ExcelField(title="是否匿名", dictType="yes_no", align=2, sort=9)
	public String getYesNo() {
		return yesNo;
	}

	public void setYesNo(String yesNo) {
		this.yesNo = yesNo;
	}
	
	@ExcelField(title="名称", align=2, sort=10)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="备用字段1", align=2, sort=11)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public String getDescCon() {
		return descCon;
	}

	public void setDescCon(String descCon) {
		this.descCon = descCon;
	}

	public String getServiceAtt() {
		return serviceAtt;
	}

	public void setServiceAtt(String serviceAtt) {
		this.serviceAtt = serviceAtt;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
}