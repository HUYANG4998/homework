/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemnotice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.systemnotice.entity.SystemNotice;
import com.jeeplus.modules.shop.systemnotice.mapper.SystemNoticeMapper;

/**
 * 系统通知Service
 * @author lhh
 * @version 2019-11-21
 */
@Service
@Transactional(readOnly = true)
public class SystemNoticeService extends CrudService<SystemNoticeMapper, SystemNotice> {

	public SystemNotice get(String id) {
		return super.get(id);
	}
	
	public List<SystemNotice> findList(SystemNotice systemNotice) {
		return super.findList(systemNotice);
	}
	
	public Page<SystemNotice> findPage(Page<SystemNotice> page, SystemNotice systemNotice) {
		return super.findPage(page, systemNotice);
	}
	
	@Transactional(readOnly = false)
	public void save(SystemNotice systemNotice) {
		super.save(systemNotice);
	}
	
	@Transactional(readOnly = false)
	public void delete(SystemNotice systemNotice) {
		super.delete(systemNotice);
	}
	
}