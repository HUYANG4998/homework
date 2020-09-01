/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemyin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.systemyin.entity.SystemYin;
import com.jeeplus.modules.shop.systemyin.mapper.SystemYinMapper;

/**
 * 隐私政策Service
 * @author lhh
 * @version 2019-11-27
 */
@Service
@Transactional(readOnly = true)
public class SystemYinService extends CrudService<SystemYinMapper, SystemYin> {

	public SystemYin get(String id) {
		return super.get(id);
	}
	
	public List<SystemYin> findList(SystemYin systemYin) {
		return super.findList(systemYin);
	}
	
	public Page<SystemYin> findPage(Page<SystemYin> page, SystemYin systemYin) {
		return super.findPage(page, systemYin);
	}
	
	@Transactional(readOnly = false)
	public void save(SystemYin systemYin) {
		super.save(systemYin);
	}
	
	@Transactional(readOnly = false)
	public void delete(SystemYin systemYin) {
		super.delete(systemYin);
	}
	
}