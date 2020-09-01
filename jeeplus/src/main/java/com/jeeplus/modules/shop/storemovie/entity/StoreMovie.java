/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storemovie.entity;

import com.jeeplus.modules.shop.store.entity.Store;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import javax.persistence.Transient;
import java.util.List;

/**
 * 商家动态Entity
 * @author lhh
 * @version 2019-12-13
 */
public class StoreMovie extends DataEntity<StoreMovie> {

	private static final long serialVersionUID = 1L;
	private Store store;		// 商家名称
	private String title;		// 标题
	private String movie;		// 视频
	private String movieImg;		// 封面
	private String dianNum;		// 点赞个数
	private String pingNum;		// 评论个数
	private String byOne;		// 备用字段1
	private String byTwo;		// 备用字段2
	private String byThree;		// 备用字段3
	private String isAssist;		// 前端专用 展示用判断是否点赞
	private String isFllow;		// 前端专用 判断是否关注店铺
	private List<String> idList;

	public StoreMovie() {
		super();
	}

	public StoreMovie(String id){
		super(id);
	}

	@ExcelField(title="商家名称", fieldType=Store.class, value="store.name", align=2, sort=2)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@ExcelField(title="标题", align=2, sort=3)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ExcelField(title="视频", align=2, sort=4)
	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	@ExcelField(title="封面", align=2, sort=5)
	public String getMovieImg() {
		return movieImg;
	}

	public void setMovieImg(String movieImg) {
		this.movieImg = movieImg;
	}

	@ExcelField(title="点赞个数", align=2, sort=6)
	public String getDianNum() {
		return dianNum;
	}

	public void setDianNum(String dianNum) {
		this.dianNum = dianNum;
	}

	@ExcelField(title="评论个数", align=2, sort=7)
	public String getPingNum() {
		return pingNum;
	}

	public void setPingNum(String pingNum) {
		this.pingNum = pingNum;
	}

	@ExcelField(title="备用字段1", align=2, sort=8)
	public String getByOne() {
		return byOne;
	}

	public void setByOne(String byOne) {
		this.byOne = byOne;
	}

	@ExcelField(title="备用字段2", align=2, sort=9)
	public String getByTwo() {
		return byTwo;
	}

	public void setByTwo(String byTwo) {
		this.byTwo = byTwo;
	}

	@ExcelField(title="备用字段3", align=2, sort=10)
	public String getByThree() {
		return byThree;
	}

	public void setByThree(String byThree) {
		this.byThree = byThree;
	}
	@Transient
	public String getIsAssist() {
		return isAssist;
	}

	public void setIsAssist(String isAssist) {
		this.isAssist=isAssist;
	}
	@Transient
	public String getIsFllow() {
		return isFllow;
	}

	public void setIsFllow(String isFllow) {
		this.isFllow=isFllow;
	}

	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}
}
