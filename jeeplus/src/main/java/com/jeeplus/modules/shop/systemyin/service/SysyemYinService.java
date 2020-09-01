/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemyin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.systemyin.entity.SysyemYin;
import com.jeeplus.modules.shop.systemyin.mapper.SysyemYinMapper;

/**
 * 隐私政策Service
 * @author lhh
 * @version 2019-11-27
 */
@Service
@Transactional(readOnly = true)
public class SysyemYinService extends CrudService<SysyemYinMapper, SysyemYin> {

	public SysyemYin get(String id) {
		return super.get(id);
	}
	
	public List<SysyemYin> findList(SysyemYin sysyemYin) {
		return super.findList(sysyemYin);
	}
	
	public Page<SysyemYin> findPage(Page<SysyemYin> page, SysyemYin sysyemYin) {
		return super.findPage(page, sysyemYin);
	}
	
	@Transactional(readOnly = false)
	public void save(SysyemYin sysyemYin) {
		super.save(sysyemYin);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysyemYin sysyemYin) {
		super.delete(sysyemYin);
	}
	
}