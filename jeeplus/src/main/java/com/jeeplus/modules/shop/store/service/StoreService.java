/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.store.service;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.mapper.StoreMapper;

import javax.annotation.Resource;

/**
 * 商家管理Service
 * @author lhh
 * @version 2019-12-20
 */
@Service
@Transactional(readOnly = true)
public class StoreService extends CrudService<StoreMapper, Store> {
	@Resource
	private StoreMapper storeMapper;

	public Store get(String id) {
		return super.get(id);
	}
	
	public List<Store> findList(Store store) {
		return super.findList(store);
	}
	
	public Page<Store> findPage(Page<Store> page, Store store) {
		return super.findPage(page, store);
	}
	
	@Transactional(readOnly = false)
	public void save(Store store) {
		super.save(store);
	}
	
	@Transactional(readOnly = false)
	public void delete(Store store) {
		super.delete(store);
	}

	public Store likeUserId(String userId) {
		Store store=null;
		if(!StringUtils.isEmpty(userId)){
			store=storeMapper.likeUserId(userId);
		}
		return store;
	}
	public Page<Store> getStoreAndWares(Page<Store> page,Store store){
		dataRuleFilter(store);
		store.setPage(page);
		List<Store> storeAndWares = storeMapper.getStoreAndWares(store);
		page.setList(storeAndWares);
		return page;
	}
}