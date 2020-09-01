/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.waresspecs.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.waresspecs.entity.WaresSpecs;
import com.jeeplus.modules.shop.waresspecs.mapper.WaresSpecsMapper;

import javax.annotation.Resource;

/**
 * 商品规格Service
 * @author lhh
 * @version 2019-12-02
 */
@Service
@Transactional(readOnly = true)
public class WaresSpecsService extends CrudService<WaresSpecsMapper, WaresSpecs> {
	@Resource
	private WaresSpecsMapper waresSpecsMapper;

	public WaresSpecs get(String id) {
		return super.get(id);
	}
	
	public List<WaresSpecs> findList(WaresSpecs waresSpecs) {
		return super.findList(waresSpecs);
	}
	
	public Page<WaresSpecs> findPage(Page<WaresSpecs> page, WaresSpecs waresSpecs) {
		return super.findPage(page, waresSpecs);
	}
	
	@Transactional(readOnly = false)
	public void save(WaresSpecs waresSpecs) {
		super.save(waresSpecs);
	}
	
	@Transactional(readOnly = false)
	public void delete(WaresSpecs waresSpecs) {
		super.delete(waresSpecs);
	}

	@Transactional(readOnly = false)
	public void deleteByWaresId(Map<String, Object> m) {
		if(m!=null&&!m.isEmpty()){
			waresSpecsMapper.deleteByWaresId(m);
		}
	}
}