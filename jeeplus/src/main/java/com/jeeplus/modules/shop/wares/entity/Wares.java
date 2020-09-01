/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.wares.entity;

import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.warestype.entity.WaresType;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import org.bytedeco.javacpp.presets.opencv_core;

import javax.persistence.Transient;
import java.util.List;

/**
 * 商品管理Entity
 * @author lhh
 * @version 2019-12-18
 */
public class Wares extends DataEntity<Wares> {

	private static final long serialVersionUID = 1L;
	private Store store;		// 商家名称
	private String name;		// 商品名称
	private WaresType waresType;		// 所属分类
	private String title;		// 简介
	private String price;		// 价格
	private String movie;		// 视频
	private String img;		// 主图
	private String description;		// 介绍
	private String dImg;		// 商品详情（图）
	private String isShang;		// 是否上架（0否1是）
	private String isYou;		// 是否优惠（0否1是）
	private String saleNum;		// 总销量
	private String byOne;		// 平台分类
	private String stock;		// 备用字段2
	private String byThree;		// 备用字段3
	private String attributeList;		// 属性集合（前端展示规格专用）
	private List<String> idList;		// 商家id集合（首页查询商品专用）

	public Wares() {
		super();
	}

	public Wares(String id){
		super(id);
	}



	@ExcelField(title="商家名称", fieldType=Store.class, value="store.name", align=2, sort=6)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@ExcelField(title="商品名称", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="所属分类", fieldType=WaresType.class, value="waresType.name", align=2, sort=8)
	public WaresType getWaresType() {
		return waresType;
	}

	public void setWaresType(WaresType waresType) {
		this.waresType = waresType;
	}

	@ExcelField(title="简介", align=2, sort=9)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ExcelField(title="价格", align=2, sort=10)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@ExcelField(title="视频", align=2, sort=11)
	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	@ExcelField(title="主图", align=2, sort=12)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@ExcelField(title="介绍", align=2, sort=13)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ExcelField(title="商品详情（图）", align=2, sort=14)
	public String getdImg() {
		return dImg;
	}

	public void setdImg(String dImg) {
		this.dImg = dImg;
	}

	@ExcelField(title="是否上架（0否1是）", dictType="yes_no", align=2, sort=15)
	public String getIsShang() {
		return isShang;
	}

	public void setIsShang(String isShang) {
		this.isShang = isShang;
	}

	@ExcelField(title="是否优惠（0否1是）", dictType="yes_no", align=2, sort=16)
	public String getIsYou() {
		return isYou;
	}

	public void setIsYou(String isYou) {
		this.isYou = isYou;
	}

	@ExcelField(title="总销量", align=2, sort=17)
	public String getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(String saleNum) {
		this.saleNum = saleNum;
	}

	@ExcelField(title="备用字段1", align=2, sort=18)
	public String getByOne() {
		return byOne;
	}

	public void setByOne(String byOne) {
		this.byOne = byOne;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	@ExcelField(title="备用字段3", align=2, sort=20)
	public String getByThree() {
		return byThree;
	}

	public void setByThree(String byThree) {
		this.byThree = byThree;
	}

	@ExcelField(title="属性集合（前端展示规格专用）", align=2, sort=21)
	public String getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(String attributeList) {
		this.attributeList = attributeList;
	}

	@Transient
	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList=idList;
	}
}
