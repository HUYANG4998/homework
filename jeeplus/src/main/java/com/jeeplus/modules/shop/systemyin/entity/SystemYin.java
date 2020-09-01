/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemyin.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 隐私政策Entity
 * @author lhh
 * @version 2019-11-27
 */
public class SystemYin extends DataEntity<SystemYin> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String content;		// 内容
	private String type;		// 类型(1:用户2:商家3:骑手)
	
	public SystemYin() {
		super();
	}

	public SystemYin(String id){
		super(id);
	}

	@ExcelField(title="标题", align=2, sort=1)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="内容", align=2, sort=2)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@ExcelField(title="类型(1:用户2:商家3:骑手)", dictType="type", align=2, sort=3)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}