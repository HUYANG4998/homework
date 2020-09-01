/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.warestypeattributekey.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.warestypeattributekey.entity.WaresTypeAttributeKey;
import com.jeeplus.modules.shop.warestypeattributekey.mapper.WaresTypeAttributeKeyMapper;

/**
 * 分类属性Service
 * @author lhh
 * @version 2019-12-24
 */
@Service
@Transactional(readOnly = true)
public class WaresTypeAttributeKeyService extends CrudService<WaresTypeAttributeKeyMapper, WaresTypeAttributeKey> {

	public WaresTypeAttributeKey get(String id) {
		return super.get(id);
	}
	
	public List<WaresTypeAttributeKey> findList(WaresTypeAttributeKey waresTypeAttributeKey) {
		return super.findList(waresTypeAttributeKey);
	}
	
	public Page<WaresTypeAttributeKey> findPage(Page<WaresTypeAttributeKey> page, WaresTypeAttributeKey waresTypeAttributeKey) {
		return super.findPage(page, waresTypeAttributeKey);
	}
	
	@Transactional(readOnly = false)
	public void save(WaresTypeAttributeKey waresTypeAttributeKey) {
		super.save(waresTypeAttributeKey);
	}
	
	@Transactional(readOnly = false)
	public void delete(WaresTypeAttributeKey waresTypeAttributeKey) {
		super.delete(waresTypeAttributeKey);
	}
	
}