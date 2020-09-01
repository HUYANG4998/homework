/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.warestypeattributevalue.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.warestypeattributevalue.entity.WaresTypeAttributeValue;
import com.jeeplus.modules.shop.warestypeattributevalue.mapper.WaresTypeAttributeValueMapper;

/**
 * 分类属性值Service
 * @author lhh
 * @version 2019-12-02
 */
@Service
@Transactional(readOnly = true)
public class WaresTypeAttributeValueService extends CrudService<WaresTypeAttributeValueMapper, WaresTypeAttributeValue> {

	public WaresTypeAttributeValue get(String id) {
		return super.get(id);
	}
	
	public List<WaresTypeAttributeValue> findList(WaresTypeAttributeValue waresTypeAttributeValue) {
		return super.findList(waresTypeAttributeValue);
	}
	
	public Page<WaresTypeAttributeValue> findPage(Page<WaresTypeAttributeValue> page, WaresTypeAttributeValue waresTypeAttributeValue) {
		return super.findPage(page, waresTypeAttributeValue);
	}
	
	@Transactional(readOnly = false)
	public void save(WaresTypeAttributeValue waresTypeAttributeValue) {
		super.save(waresTypeAttributeValue);
	}
	
	@Transactional(readOnly = false)
	public void delete(WaresTypeAttributeValue waresTypeAttributeValue) {
		super.delete(waresTypeAttributeValue);
	}
	
}