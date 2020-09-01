/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemtype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.systemtype.entity.SystemType;
import com.jeeplus.modules.shop.systemtype.mapper.SystemTypeMapper;

/**
 * 平台分设置Service
 * @author lhh
 * @version 2020-01-16
 */
@Service
@Transactional(readOnly = true)
public class SystemTypeService extends CrudService<SystemTypeMapper, SystemType> {

	public SystemType get(String id) {
		return super.get(id);
	}
	
	public List<SystemType> findList(SystemType systemType) {
		return super.findList(systemType);
	}
	
	public Page<SystemType> findPage(Page<SystemType> page, SystemType systemType) {
		return super.findPage(page, systemType);
	}
	
	@Transactional(readOnly = false)
	public void save(SystemType systemType) {
		super.save(systemType);
	}
	
	@Transactional(readOnly = false)
	public void delete(SystemType systemType) {
		super.delete(systemType);
	}
	
}