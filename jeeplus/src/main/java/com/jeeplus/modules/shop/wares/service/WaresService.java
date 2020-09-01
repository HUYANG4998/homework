/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.wares.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.mapper.WaresMapper;

/**
 * 商品管理Service
 * @author lhh
 * @version 2019-12-18
 */
@Service
@Transactional(readOnly = true)
public class WaresService extends CrudService<WaresMapper, Wares> {

	@Autowired
	WaresMapper waresMapper;
	public Wares get(String id) {
		return super.get(id);
	}

	public List<Wares> findList(Wares wares) {
		return super.findList(wares);
	}

	public Page<Wares> findPage(Page<Wares> page, Wares wares) {
		return super.findPage(page, wares);
	}
	public Page<Wares> findPages(Page<Wares> page, Wares wares) {
		wares.setPage(page);
		page.setList(waresMapper.findWaresByStore(wares));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(Wares wares) {
		super.save(wares);
	}

	@Transactional(readOnly = false)
	public void delete(Wares wares) {
		super.delete(wares);
	}

    public List<Wares> getWaresByStoreIdAndName(String id, String name) {
		List<Wares> listWares=new ArrayList<>();
		if(!StringUtils.isEmpty(id)&&!StringUtils.isEmpty(name)){
			listWares=waresMapper.getWaresByStoreIdAndName(id,name);
		}
		return listWares;
    }
}
