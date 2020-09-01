/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.warestype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.warestype.entity.WaresType;
import com.jeeplus.modules.shop.warestype.mapper.WaresTypeMapper;

/**
 * 商品分类Service
 * @author lhh
 * @version 2019-12-02
 */
@Service
@Transactional(readOnly = true)
public class WaresTypeService extends CrudService<WaresTypeMapper, WaresType> {

	public WaresType get(String id) {
		return super.get(id);
	}
	
	public List<WaresType> findList(WaresType waresType) {
		return super.findList(waresType);
	}
	
	public Page<WaresType> findPage(Page<WaresType> page, WaresType waresType) {
		return super.findPage(page, waresType);
	}
	
	@Transactional(readOnly = false)
	public void save(WaresType waresType) {
		super.save(waresType);
	}
	
	@Transactional(readOnly = false)
	public void delete(WaresType waresType) {
		super.delete(waresType);
	}
	
}