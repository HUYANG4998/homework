/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemnotice.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 系统通知Entity
 * @author lhh
 * @version 2019-11-21
 */
public class SystemNotice extends DataEntity<SystemNotice> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String content;		// 内容
	
	public SystemNotice() {
		super();
	}

	public SystemNotice(String id){
		super(id);
	}

	@ExcelField(title="标题", align=2, sort=2)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="内容", align=2, sort=3)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}